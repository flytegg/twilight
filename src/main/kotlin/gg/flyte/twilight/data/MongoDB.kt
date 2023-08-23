package gg.flyte.twilight.data

import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.environment.Environment
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object MongoDB {

    private val codecRegistry = fromRegistries(
        getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase

    fun mongo(mongo: Settings) {
        client = MongoClients.create(mongo.uri)
        database = client.getDatabase(mongo.database + if (Twilight.usingEnv && Environment.isDev()) "-dev" else "")
            .withCodecRegistry(codecRegistry)
    }

    fun collection(name: String): MongoCollection<Document> {
        return database.getCollection(name)
    }

    fun <T> collection(name: String, `class`: Class<T>): MongoCollection<T> {
        return database.getCollection(name, `class`)
    }

    class Settings {
        var uri: String = if (Twilight.usingEnv) Environment.get("MONGO_URI") else ""
        var database: String = if (Twilight.usingEnv) Environment.get("MONGO_DATABASE") else ""
    }

}