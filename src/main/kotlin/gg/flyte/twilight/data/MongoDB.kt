package gg.flyte.twilight.data

import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.client.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.gson.GSON
import gg.flyte.twilight.gson.toJson
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

    private val idField = IdField(clazz)
    val name = clazz.simpleName!!.pluralize().formatCase(Case.CAMEL)

    val documents: MongoCollection<Document> = MongoDB.database.getCollection(name, Document::class.java)

    fun save(serializable: MongoSerializable): UpdateResult = with(serializable.toDocument()) {
        documents.replaceOne(eq(idField.name, this[idField.name]), this, ReplaceOptions().upsert(true))
    }

    fun find(filter: Bson): MongoIterable<out MongoSerializable> =
        documents.find(filter).map { GSON.fromJson(it.toJson(), clazz.java) }

    fun findById(id: Any): MongoIterable<out MongoSerializable> {
        require(id::class.javaObjectType == idField.type.javaType) {
            "ID must be of type ${idField.type} (Java: ${idField.type.javaType})"
        }
        return find(eq(idField.name, id))
    }

    fun delete(filter: Bson): DeleteResult = documents.deleteMany(filter)

    fun deleteById(id: Any): DeleteResult {
        require(id::class.javaObjectType == idField.type.javaType) {
            "ID must be of type ${idField.type} (Java: ${idField.type.javaType})"
        }
        return delete(eq(idField.name, id))
    }

}

interface MongoSerializable {
    fun save(): UpdateResult = MongoDB.collection(this::class).save(this)

    fun delete(): DeleteResult = MongoDB.collection(this::class).delete(eq(IdField(this).name, IdField(this).value))

    fun toDocument(): Document = Document.parse(toJson())
}

@Target(AnnotationTarget.FIELD)
annotation class Id

data class IdField(val clazz: KClass<out MongoSerializable>, val instance: MongoSerializable? = null) {

    constructor(instance: MongoSerializable) : this(instance::class, instance)

    val name: String
    val type: KType
    var value: Any? = null

    init {
        val idFields = clazz.memberProperties.filter { it.javaField?.isAnnotationPresent(Id::class.java) == true }

        require(idFields.size == 1) {
            when (idFields.size) {
                0 -> "Class does not have a field annotated with @Id"
                else -> "Class must not have more than one field annotated with @Id"
            }
        }

        val idField = idFields.first()

        name = idField.name
        type = idField.returnType

        @Suppress("unchecked_cast")
        if (instance != null) {
            value = (idField as KProperty1<Any, *>).get(instance)
                ?: throw IllegalStateException("Field annotated with @Id must not be null")
        }
    }

}

fun Any.toDocument(): Document = Document.parse(toJson())

infix fun <V> KProperty<V>.eq(other: Any): Bson = eq(this.name, other)