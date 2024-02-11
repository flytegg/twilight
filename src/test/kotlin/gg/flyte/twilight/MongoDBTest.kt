package gg.flyte.twilight

import gg.flyte.twilight.data.Id
import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.MongoSerializable
import gg.flyte.twilight.data.eq
import java.util.*

fun main() {
    MongoDB.mongo(MongoDB.Settings().apply {
        uri = "mongodb://localhost:27017"
        database = "test"
    })

    testCollectionCache()
    testSaving()
    testFind()
}

fun testCollectionCache() {
    (0..10).forEach { _ ->
//        println(MongoDB.collections) - was made private
        MongoDB.collection(StandardTestClass::class)
    }
}

fun testSaving() {
    runCatching { StandardTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { NameAsIdTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { NoIdTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { MultipleIdTestClass().save() }.onFailure { it.printStackTrace() }
    runCatching { NullIdTestClass().save() }.onFailure { it.printStackTrace() }
}

fun testFind() {
    runCatching {
        with(MongoDB.collection(NameAsIdTestClass::class)) {
            println("TwilightMongoCollection#findById")
            runCatching {
                with(findById("Test2")) {
                    println("Found ${count()} documents")
                    forEach { println(it) }
                    println("Done")
                }
            }.onFailure { it.printStackTrace() }

            println("TwilightMongoCollection#findById")
            runCatching {
                with(findById(123)) {
                    println("Found ${count()} documents")
                    forEach { println(it) }
                    println("Done")
                }
            }.onFailure { it.printStackTrace() }

            println("TwilightMongoCollection#find")
            runCatching {
                with(find(NameAsIdTestClass::name eq "Test2")) {
                    println("Found ${count()} documents")
                    forEach { println(it) }
                    println("Done")
                }
            }.onFailure { it.printStackTrace() }
        }

        with(MongoDB.collection(StandardTestClass::class)) {
            println("TwilightMongoCollection#find (no filter)")
            runCatching {
                with(find()) {
                    println("Found ${count()} documents")
                    forEach {
                        println(it)
                    }
                    println("Done")
                }
            }.onFailure { it.printStackTrace() }
        }
    }.onFailure { it.printStackTrace() }
}

data class StandardTestClass(
    @field:Id val id: UUID = UUID.randomUUID(),
    val name: String = "Test1",
) : MongoSerializable

data class NameAsIdTestClass(
    val id: UUID = UUID.randomUUID(),
    @field:Id val name: String = "Test2",
) : MongoSerializable

data class NoIdTestClass(
    val id: UUID = UUID.randomUUID(),
    val name: String = "Test3",
) : MongoSerializable

data class MultipleIdTestClass(
    @field:Id val id: UUID = UUID.randomUUID(),
    @field:Id val name: String = "Test4",
) : MongoSerializable

data class NullIdTestClass(
    @field:Id val id: UUID? = null,
    val name: String = "Test5",
) : MongoSerializable
