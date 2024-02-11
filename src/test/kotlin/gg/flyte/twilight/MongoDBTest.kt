package gg.flyte.twilight

import gg.flyte.twilight.data.Id
import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.MongoSerializable
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    MongoDB.mongo(MongoDB.Settings().apply {
        uri = "mongodb://localhost:27017"
        database = "test"
    })

    val sample = StandardTestClass()
    sample.save()

//    testCollectionCache()
//    testSaving()
//  testFind()
}

/*fun testCollectionCache() {
    val iterations = 1000
    var millisTotal = 0L
    var firstMillis = 0L
    var secondMillis = 0L
    (0..iterations).forEach { i ->
        measureTimeMillis {
            MongoDB.collection(StandardTestClass::class)
        }.also {
            millisTotal += it
            if (i == 0) firstMillis = it
            if (i == 1) secondMillis = it
        }
    }
    println("Average time to get a collection: ${millisTotal / iterations}ms")
    println("First time to get a collection: ${firstMillis}ms")
    println("Second time to get a collection: ${secondMillis}ms")
}

fun testSaving() {
    val iterations = 1000
    var millisTotal = 0L
    val sample = StandardTestClass()
    (0..iterations).forEach { _ ->
        measureTimeMillis {
            runCatching { sample.save() }.onFailure { it.printStackTrace() }
        }.also { millisTotal += it }
    }
    println("Average time to save a document: ${millisTotal / iterations}ms")
    *//*
      runCatching { NameAsIdTestClass().save() }.onFailure { it.printStackTrace() }
      runCatching { NoIdTestClass().save() }.onFailure { it.printStackTrace() }
      runCatching { MultipleIdTestClass().save() }.onFailure { it.printStackTrace() }
      runCatching { NullIdTestClass().save() }.onFailure { it.printStackTrace() }*//*
}*/

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
