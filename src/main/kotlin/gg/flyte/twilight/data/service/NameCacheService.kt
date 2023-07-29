package gg.flyte.twilight.data.service

import com.google.gson.JsonParser
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
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

    private val cache = mutableMapOf<UUID, String>()
    private val mongoCache = MongoDB.collection(Environment.get("NAME_CACHE_COLLECTION"))

    fun nameFromUUID(uuid: UUID): String {
        return cache[uuid] ?: queryMongoNameByUUID(
            uuid
        ) ?: queryMojangNameByUUID(uuid)
    }

    private fun queryMongoNameByUUID(uuid: UUID): String? {
        mongoCache.let {
            mongoCache.find(Filters.eq("_id", uuid.toString())).first()
                ?.let {
                    val name =
                        it.getString("name") ?: throw MongoException("Document with '_id' '$uuid' has no field 'name'.")
                    cache[uuid] = name
                    return name
                } ?: return null
        }
    }

    private fun queryMojangNameByUUID(uuid: UUID): String {
        val connection =
            URL("${Environment.get("MOJANG_PROFILE_ENDPOINT")}/$uuid").openConnection() as HttpsURLConnection
        connection.doOutput = true
        val name = JsonParser.parseReader(connection.inputStream.reader()).asJsonObject.get("name").asString
        connection.disconnect()
        cache[uuid] = name
        mongoCache.insertOne(
            Document(
                mapOf(
                    "_id" to uuid.toString(),
                    "name" to name
                )
            )
        )
        return name
    }

    fun uuidFromName(name: String): UUID {
        return cache.findKeyByValue(name)
            ?: queryMongoUUIDByName(
                name
            ) ?: queryMojangUUIDByName(name)
    }

    private fun queryMongoUUIDByName(name: String): UUID? {
        mongoCache.let {
            mongoCache.find(
                Filters.regex(
                    "name",
                    Pattern.compile("^$name$", Pattern.CASE_INSENSITIVE)
                )
            ).first()?.let {
                val uuid = UUID.fromString(it.getString("_id"))
                    ?: throw MongoException("Document with 'name' '$name' has no valid UUID at '_id'.")
                cache[uuid] = name
                return uuid
            } ?: return null
        }
    }

    private fun queryMojangUUIDByName(name: String): UUID {
        val connection =
            URL("${Environment.get("MOJANG_UUID_ENDPOINT")}/$name").openConnection() as HttpsURLConnection
        connection.doOutput = true
        val uuid = JsonParser.parseReader(connection.inputStream.reader()).asJsonObject.get("id").asString.toUUID()
        connection.disconnect()
        cache[uuid] = name
        mongoCache.insertOne(
            Document(
                mapOf(
                    "_id" to uuid.toString(),
                    "name" to name
                )
            )
        )
        return uuid
    }

}