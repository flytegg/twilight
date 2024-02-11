package gg.flyte.twilight.data

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoCollection
import com.mongodb.kotlin.client.MongoDatabase
import com.mongodb.kotlin.client.MongoIterable
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.data.MongoDB.executor
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.gson.GSON
import gg.flyte.twilight.gson.toJson
import gg.flyte.twilight.string.Case
import gg.flyte.twilight.string.formatCase
import gg.flyte.twilight.string.pluralize
import org.bson.Document
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.conversions.Bson
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaType

object MongoDB {

    private lateinit var client: MongoClient
    internal lateinit var database: MongoDatabase
    internal val executor: Executor = Executors.newCachedThreadPool()

    fun mongo(mongo: Settings) {
        client = MongoClient.create(
            MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(fromProviders(getDefaultCodecRegistry()))
                .applyConnectionString(ConnectionString(mongo.uri))
                .build()
        )
        database = client.getDatabase(mongo.database + if (Twilight.usingEnv && Environment.isDev()) "-dev" else "")
    }

    fun collection(name: String): MongoCollection<Document> = database.getCollection(name)

    @Deprecated("Use collection(clazz: KClass<out MongoSerializable>)", ReplaceWith("collection(`class`)"))
    fun <T : Any> collection(name: String, `class`: Class<T>): MongoCollection<T> =
        database.getCollection(name, `class`)

    class Settings {
        var uri: String = if (Twilight.usingEnv) Environment.get("MONGO_URI") else ""
        var database: String = if (Twilight.usingEnv) Environment.get("MONGO_DATABASE") else ""
    }

    private val collections = mutableMapOf<KClass<out MongoSerializable>, TwilightMongoCollection>()

    fun collection(
        clazz: KClass<out MongoSerializable>,
        name: String = clazz.simpleName!!.pluralize().formatCase(Case.CAMEL)
    ): TwilightMongoCollection =
        collections.getOrPut(clazz) { TwilightMongoCollection(clazz, name) }

}

class TwilightMongoCollection(private val clazz: KClass<out MongoSerializable>, val name: String) {

    val idField = IdField(clazz)
    val documents: MongoCollection<Document> = MongoDB.database.getCollection(name, Document::class.java)

    fun saveSync(serializable: MongoSerializable): UpdateResult = with(serializable.toDocument()) {
        documents.replaceOne(
            eq(idField.name, this[idField.name]),
            this,
            ReplaceOptions().upsert(true)
        )
    }

    fun save(serializable: MongoSerializable): CompletableFuture<UpdateResult> =
        CompletableFuture.supplyAsync({ saveSync(serializable) }, executor)

    fun <T : MongoSerializable> findSync(filter: Bson? = null): MongoIterable<T> =
        (if (filter == null) documents.find() else documents.find(filter)).map {
            @Suppress("unchecked_cast")
            GSON.fromJson(it.toJson(), clazz.java) as T
        }

    fun <T : MongoSerializable> find(filter: Bson? = null): CompletableFuture<MongoIterable<T>> =
        CompletableFuture.supplyAsync({ findSync(filter) }, executor)

    fun <T : MongoSerializable> findById(id: Any): CompletableFuture<MongoIterable<T>> {
        require(id::class.javaObjectType == idField.type.javaType) {
            "id must be of type ${idField.type} (Java: ${idField.type.javaType})"
        }
        return find(eq(idField.name, id))
    }

    fun deleteSync(filter: Bson): DeleteResult = documents.deleteMany(filter)

    fun delete(filter: Bson): CompletableFuture<DeleteResult> =
        CompletableFuture.supplyAsync({ deleteSync(filter) }, executor)

    fun deleteById(id: Any): CompletableFuture<DeleteResult> {
        require(id::class.javaObjectType == idField.type.javaType) {
            "id must be of type ${idField.type} (Java: ${idField.type.javaType})"
        }
        return delete(eq(idField.name, id))
    }

}

interface MongoSerializable {
    fun saveSync(): UpdateResult = MongoDB.collection(this::class).saveSync(this)

    fun save(): CompletableFuture<UpdateResult> = MongoDB.collection(this::class).save(this)

    fun deleteSync(): DeleteResult = with(MongoDB.collection(this::class)) {
        deleteSync(eq(idField.name, idField.value(this@MongoSerializable)))
    }

    fun delete(): CompletableFuture<DeleteResult> = with(MongoDB.collection(this::class)) {
        delete(eq(idField.name, idField.value(this@MongoSerializable)))
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