package gg.flyte.twilight.data

import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
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
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

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

    fun collection(name: String): MongoCollection<Document> {
        return database.getCollection(name)
    }

    @Deprecated("Use collection(clazz: KClass<out MongoSerializable>)", ReplaceWith("collection(`class`)"))
    fun <T> collection(name: String, `class`: Class<T>): MongoCollection<T> {
        return database.getCollection(name, `class`)
    }

    class Settings {
        var uri: String = if (Twilight.usingEnv) Environment.get("MONGO_URI") else ""
        var database: String = if (Twilight.usingEnv) Environment.get("MONGO_DATABASE") else ""
    }

    val collections = mutableMapOf<KClass<out MongoSerializable>, TwilightMongoCollection>()

    fun collection(clazz: KClass<out MongoSerializable>): TwilightMongoCollection =
        collections.getOrPut(clazz) { TwilightMongoCollection(clazz.simpleName!!.pluralize().formatCase(Case.CAMEL)) }

}

class TwilightMongoCollection(name: String) {

    val documents: MongoCollection<Document> = MongoDB.database.getCollection(name, Document::class.java)

    fun save(serializable: MongoSerializable): UpdateResult = with(serializable.toDocument()) {
        documents.replaceOne(eq("_id", this["_id"]), this, ReplaceOptions().upsert(true))
    }

    // TODO: Implement more methods for querying, updating, and deleting documents - for now just keep exposing the underlying collection as documents

}

interface MongoSerializable {

    fun save(): UpdateResult = MongoDB.collection(this::class).save(this)

    fun toDocument(): Document = Document.parse(GSON.toJsonTree(this, this::class.java).asJsonObject.run {
        IdField(this@MongoSerializable).let {
            remove(it.name)
            add("_id", GSON.toJsonTree(it.value))
        }
        toString()
    })

}

@Target(AnnotationTarget.FIELD)
annotation class Id

data class IdField(val clazz: KClass<out MongoSerializable>, val instance: MongoSerializable? = null) {
    constructor(instance: MongoSerializable) : this(instance::class, instance)

    val name: String
    var value: Any? = null

    init {
        val idFields = clazz.memberProperties.filter { it.javaField?.isAnnotationPresent(Id::class.java) == true }

        require(idFields.size == 1) {
            when (idFields.size) {
                0 -> "Class does not have a field annotated with @Id"
                else -> "Class must not have more than one field annotated with @Id"
            }
        }

        name = idFields.first().name
        @Suppress("unchecked_cast")
        if (instance != null) {
            value = (idFields.first() as KProperty1<Any, *>).get(instance)
                ?: throw IllegalStateException("Field annotated with @Id must not be null")
        }
    }

}

fun Any.toDocument(): Document = Document.parse(toJson())

infix fun <V> KProperty<V>.eq(other: Any): Bson = eq(this.name, other)