package gg.flyte.twilight.data.sql

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

interface SQLSerializable {

    enum class Dialect {
        POSTGRES,
        DEFAULT,
    }

    fun toInsertQuery(tableName: String): String {
        val properties = this.javaClass.kotlin.memberProperties
        val columns = properties.joinToString(", ") { it.name }
        val values = properties.joinToString(", ") { "'${it.get(this)}'" }
        return "INSERT INTO $tableName ($columns) VALUES ($values)"
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
            append("CREATE TABLE IF NOT EXISTS ${pluralize(tableName)} (")
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
            "double", "kotlin.Double", "java.lang.Double", "kotlin.Float", "java.lang.Float" -> "REAL"
            "boolean", "kotlin.Boolean", "java.lang.Boolean" -> "BOOLEAN"
            "java.util.Date", "java.time.LocalDate", "java.time.LocalTime", "java.time.LocalDateTime", "java.sql.Date", "java.sql.Time", "java.sql.Timestamp" -> "TIMESTAMP"
            "java.net.InetAddress" -> if(dialect == Dialect.POSTGRES) "INET" else "VARCHAR(39)"
            "java.util.UUID" -> if(dialect == Dialect.POSTGRES) "UUID" else "VARCHAR(36)"
            else -> throw IllegalArgumentException("Error: Unsupported type: $kotlinType")
        }
    }

    private fun pluralize(word: String, count: Int = 2): String {
        return when {
            word.endsWith("s") || word.endsWith("x") || word.endsWith("z") || word.endsWith("ch") || word.endsWith("sh") -> {
                word + "es"
            }
            word.endsWith("y") -> word.dropLast(1) + "ies"

            word.endsWith("f") -> word.dropLast(1) + "ves"

            word.endsWith("fe") -> word.dropLast(2) + "ves"

            word.endsWith("o") -> word + "es"
            else -> word + "s"
        }
    }
}