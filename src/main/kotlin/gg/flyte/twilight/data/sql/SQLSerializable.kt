package gg.flyte.twilight.data.sql

import kotlin.reflect.full.memberProperties

interface SQLSerializable {
    fun toInsertQuery(tableName: String): String {
        val properties = this.javaClass.kotlin.memberProperties
        val columns = properties.joinToString(", ") { it.name }
        val values = properties.joinToString(", ") { "'${it.get(this)}'" }
        return "INSERT INTO $tableName ($columns) VALUES ($values);"
    }
}