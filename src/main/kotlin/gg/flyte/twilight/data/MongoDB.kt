package gg.flyte.twilight.data

import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import gg.flyte.twilight.environment.Environment
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object MongoDB {

    private val client = MongoClients.create(Environment.get("MONGO_URI"))
    private val codecRegistry = fromRegistries(
        getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )
    private val database =
        client.getDatabase(Environment.get("MONGO_DATABASE") + if (Environment.isDev()) "-dev" else "")
            .withCodecRegistry(
                codecRegistry
            )

    fun collection(name: String): MongoCollection<Document> {
        return database.getCollection(name)
    }

    fun <T> collection(name: String, `class`: Class<T>): MongoCollection<T> {
        return database.getCollection(name, `class`)
    }

}