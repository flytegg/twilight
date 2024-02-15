package gg.flyte.twilight.data.sql

import gg.flyte.twilight.string.pluralize
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

interface SQLSerializable {

    enum class Dialect {
        POSTGRES,
        DEFAULT,
    }

    fun toInsertQuery(): String {
        val properties = this.javaClass.kotlin.memberProperties
        val columns = properties.joinToString(", ") { it.name }
        val values = properties.joinToString(", ") { "'${it.get(this)}'" }
        return "INSERT INTO ${this.javaClass.simpleName.pluralize()} ($columns) VALUES ($values)"
    }


    fun convertToSQLTable(dialect: Dialect = Dialect.DEFAULT, exclude: List<String> = emptyList()): String {
        val clazz = this::class.java
        val tableName = clazz.simpleName
        val constructor = clazz.kotlin.primaryConstructor
        val fields = constructor!!.parameters.map { it.name }
        val types = clazz.kotlin.declaredMemberProperties.map { it.returnType.toString() }

        val filteredFields = fields.filterNot { exclude.contains(it) }
        val filteredTypes = types.filterIndexed { index, _ -> !exclude.contains(fields[index]) }

        return buildString {

            append("CREATE TABLE IF NOT EXISTS ${tableName.pluralize()} (")
            filteredFields.indices.joinTo(this) { i ->
                "${filteredFields[i]} ${convertKotlinTypeToSQLType(filteredTypes[i], dialect)}"
            }
            append(");")
        }
    }


    private fun convertKotlinTypeToSQLType(kotlinType: String, dialect: Dialect): String {
        return when (kotlinType) {
            "string", "kotlin.String", "java.lang.String" -> "TEXT"
            "int", "kotlin.Int", "java.lang.Integer" -> "INTEGER"
            "long", "kotlin.Long", "java.lang.Long" -> "BIGINT"
            "double", "kotlin.Double", "java.lang.Double", "kotlin.Float", "java.lang.Float" -> if(dialect == Dialect.POSTGRES) "REAL" else "DECIMAL"
            "boolean", "kotlin.Boolean", "java.lang.Boolean" -> "BOOLEAN"
            "java.util.Date", "java.time.LocalDate", "java.time.LocalTime", "java.time.LocalDateTime", "java.sql.Date", "java.sql.Time", "java.sql.Timestamp" -> "TIMESTAMP"
            "java.net.InetAddress" -> if(dialect == Dialect.POSTGRES) "INET" else "VARCHAR(43)"
            "java.util.UUID" -> if(dialect == Dialect.POSTGRES) "UUID" else "VARCHAR(36)"
            // this needs more testing. "java.util.ArrayList", "java.util.List", "java.util.Vector", "kotlin.collections.List", "kotlin.collections.ArrayList" -> if(dialect == Dialect.POSTGRES) "ARRAY" else "STRING"
            else -> throw IllegalArgumentException("Error: Unsupported type: $kotlinType")
        }
    }
}