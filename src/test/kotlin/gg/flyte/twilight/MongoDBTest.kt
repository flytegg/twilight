package gg.flyte.twilight

import gg.flyte.twilight.data.Id
import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.MongoSerializable
import java.util.UUID

fun main() {
    MongoDB.mongo(MongoDB.Settings().apply {
        uri = "mongodb://localhost:27017"
        database = "test"
    })

    // Call to the same collection multiple times and check if the collection is cached
    (0..10).forEach { _ ->
        println(MongoDB.collections)
        MongoDB.collection(StandardTestClass::class)
    }

    runCatching { StandardTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { NameAsIdTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { NoIdTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { MultipleIdTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { NullIdTestClass().save() }.onFailure { it.printStackTrace() }
}

class StandardTestClass(
    @field:Id val id: UUID = UUID.randomUUID(),
    val name: String = "Test1",
) : MongoSerializable

class NameAsIdTestClass(
    val id: UUID = UUID.randomUUID(),
    @field:Id val name: String = "Test2",
) : MongoSerializable

class NoIdTestClass(
    val id: UUID = UUID.randomUUID(),
    val name: String = "Test3",
) : MongoSerializable

class MultipleIdTestClass(
    @field:Id val id: UUID = UUID.randomUUID(),
    @field:Id val name: String = "Test4",
) : MongoSerializable

class NullIdTestClass(
    @field:Id val id: UUID? = null,
    val name: String = "Test5",
) : MongoSerializable
