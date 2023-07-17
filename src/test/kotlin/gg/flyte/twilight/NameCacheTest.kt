package gg.flyte.twilight

import gg.flyte.twilight.data.service.NameCacheService
import java.util.*

fun main() {
    cacheNameFromUUIDTest()
    cacheUUIDFromNameTest()
}

val uuid: UUID = UUID.fromString("a008c892-e7e1-48e1-8235-8aa389318b7a")
const val name: String = "stxphen"

fun cacheNameFromUUIDTest() {
    val cachedName = NameCacheService.nameFromUUID(uuid)
    println("name for uuid '$uuid' = '$cachedName'")
}

fun cacheUUIDFromNameTest() {
    val cachedUUID = NameCacheService.uuidFromName(name)
    println("uuid for name '$name' = '$cachedUUID'")
}