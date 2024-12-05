package gg.flyte.twilight.data.service

import com.google.gson.JsonParser
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.MongoCollection
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.extension.findKeyByValue
import gg.flyte.twilight.string.toUUID
import org.bson.Document
import java.net.URL
import java.util.*
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection

object NameCacheService {

    private const val MOJANG_PROFILE_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/profile"
    private const val MOJANG_UUID_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft"

    private val cache = mutableMapOf<UUID, String>()

    private var useMongoCache = true
    private lateinit var mongoCache: MongoCollection<Document>

    fun nameCache(settings: Settings) {
        useMongoCache = settings.useMongoCache
        if (useMongoCache) {
            mongoCache = MongoDB.collection(settings.collectionName)
        }
    }

    fun nameFromUUID(uuid: UUID): String {
        return cache[uuid] ?: useMongoCache.takeIf { it }?.let { queryMongoNameByUUID(uuid) } ?: queryMojangNameByUUID(uuid)
    }

    private fun queryMongoNameByUUID(uuid: UUID): String? {
        mongoCache.let {
            mongoCache.find(Filters.eq("_id", uuid.toString())).firstOrNull()
                ?.let {
                    val name =
                        it.getString("name") ?: throw MongoException("Document with '_id' '$uuid' has no field 'name'.")
                    cache[uuid] = name
                    return name
                }
        }
        return null
    }

    private fun queryMojangNameByUUID(uuid: UUID): String {
        val connection = URL("$MOJANG_PROFILE_ENDPOINT/$uuid").openConnection() as HttpsURLConnection
        connection.doOutput = true
        val name = JsonParser.parseReader(connection.inputStream.reader()).asJsonObject.get("name").asString
        connection.disconnect()
        cache[uuid] = name
        if (useMongoCache) {
            mongoCache.insertOne(
                Document(
                    mapOf(
                        "_id" to uuid.toString(),
                        "name" to name
                    )
                )
            )
        }
        return name
    }

    fun uuidFromName(name: String): UUID {
        return cache.findKeyByValue(name) ?: useMongoCache.takeIf { it }?.let { queryMongoUUIDByName(name) } ?: queryMojangUUIDByName(name)
    }

    private fun queryMongoUUIDByName(name: String): UUID? {
        mongoCache.let {
            mongoCache.find(
                Filters.regex(
                    "name",
                    Pattern.compile("^$name$", Pattern.CASE_INSENSITIVE)
                )
            ).firstOrNull()?.let {
                val uuid = UUID.fromString(it.getString("_id"))
                    ?: throw MongoException("Document with 'name' '$name' has no valid UUID at '_id'.")
                cache[uuid] = name
                return uuid
            }
        }
        return null
    }

    private fun queryMojangUUIDByName(name: String): UUID {
        val connection =
            URL("$MOJANG_UUID_ENDPOINT/$name").openConnection() as HttpsURLConnection
        connection.doOutput = true
        val uuid = JsonParser.parseReader(connection.inputStream.reader()).asJsonObject.get("id").asString.toUUID()
        connection.disconnect()
        cache[uuid] = name
        if (useMongoCache) {
            mongoCache.insertOne(
                Document(
                    mapOf(
                        "_id" to uuid.toString(),
                        "name" to name
                    )
                )
            )
        }
        return uuid
    }

    class Settings {
        var useMongoCache = true
        var collectionName: String = if (Twilight.usingEnv) Environment.get("NAME_CACHE_COLLECTION") else ("name-cache")
    }

}