package gg.flyte.twilight.data

import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.client.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.gson.GSON
import gg.flyte.twilight.gson.toJson
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.string.Case
import gg.flyte.twilight.string.formatCase
import gg.flyte.twilight.string.pluralize
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.conversions.Bson
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaType

object MongoDB {

    private val codecRegistry = fromRegistries(
        getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    private lateinit var client: MongoClient
    internal lateinit var database: MongoDatabase

    fun mongo(mongo: Settings) {
        client = MongoClients.create(mongo.uri)
        database = client.getDatabase(mongo.database + if (Twilight.usingEnv && Environment.isDev()) "-dev" else "")
            .withCodecRegistry(codecRegistry)
    }

    fun collection(name: String): MongoCollection<Document> = database.getCollection(name)

    @Deprecated("Use collection(clazz: KClass<out MongoSerializable>)", ReplaceWith("collection(`class`)"))
    fun <T> collection(name: String, `class`: Class<T>): MongoCollection<T> = database.getCollection(name, `class`)

    class Settings {
        var uri: String = if (Twilight.usingEnv) Environment.get("MONGO_URI") else ""
        var database: String = if (Twilight.usingEnv) Environment.get("MONGO_DATABASE") else ""
    }

    private val collections = mutableMapOf<KClass<out MongoSerializable>, TwilightMongoCollection>()

    fun collection(clazz: KClass<out MongoSerializable>): TwilightMongoCollection =
        collections.getOrPut(clazz) { TwilightMongoCollection(clazz) }

}

class TwilightMongoCollection(private val clazz: KClass<out MongoSerializable>) {

    val idField = IdField(clazz)
    val name = clazz.simpleName!!.pluralize().formatCase(Case.CAMEL)

    val documents: MongoCollection<Document> = MongoDB.database.getCollection(name, Document::class.java)

    fun save(serializable: MongoSerializable, async: Boolean = true) {
        delay(0, async) {
            with(serializable.toDocument()) {
                documents.replaceOne(
                    eq(idField.name, this[idField.name]),
                    this,
                    ReplaceOptions().upsert(true)
                )
            }
        }
    }

    fun find(filter: Bson? = null): MongoIterable<out MongoSerializable> =
        (if (filter == null) documents.find() else documents.find(filter)).map {
            GSON.fromJson(
                it.toJson(),
                clazz.java
            )
        }

    fun findById(id: Any): MongoIterable<out MongoSerializable> {
        require(id::class.javaObjectType == idField.type.javaType) {
            "id must be of type ${idField.type} (Java: ${idField.type.javaType})"
        }
        return find(eq(idField.name, id))
    }

    fun delete(filter: Bson, async: Boolean = true) {
        delay(0, async) {
            documents.deleteMany(filter)
        }
    }

    fun deleteById(id: Any, async: Boolean = true) {
        require(id::class.javaObjectType == idField.type.javaType) {
            "id must be of type ${idField.type} (Java: ${idField.type.javaType})"
        }
        delete(eq(idField.name, id), async)
    }

}

interface MongoSerializable {
    fun save(async: Boolean = true) = MongoDB.collection(this::class).save(this, async)

    fun delete(async: Boolean = true) = with(MongoDB.collection(this::class)) {
        delete(eq(idField.name, idField.value(this@MongoSerializable)), async)
    }

    fun toDocument(): Document = Document.parse(toJson())
}

@Target(AnnotationTarget.FIELD)
annotation class Id

data class IdField(val clazz: KClass<out MongoSerializable>) {

    private val idField: KProperty1<out MongoSerializable, *>
    val name: String
    val type: KType

    init {
        val idFields = clazz.memberProperties.filter { it.javaField?.isAnnotationPresent(Id::class.java) == true }

        require(idFields.size == 1) {
            when (idFields.size) {
                0 -> "Class does not have a field annotated with @Id"
                else -> "Class must not have more than one field annotated with @Id"
            }
        }

        idField = idFields.first()

        name = idField.name
        type = idField.returnType
    }

    @Suppress("unchecked_cast")
    fun value(instance: MongoSerializable): Any = (idField as KProperty1<Any, *>).get(instance)
        ?: throw IllegalStateException("Field annotated with @Id must not be null")

}

fun Any.toDocument(): Document = Document.parse(toJson())

infix fun <V> KProperty<V>.eq(other: Any): Bson = eq(this.name, other)