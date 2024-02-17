package gg.flyte.twilight.data.sql

import java.sql.ResultSet

data class Results(val columns: List<String>, val rows: List<Map<String, Any?>>) {
    operator fun get(column: String): List<Any?> {
        val colIndex = column.indexOf(column)
        if (colIndex == -1) throw IllegalArgumentException("Column '$column' was not found in the query result.")
        return rows.map { it[column] }
    }
}

inline fun <reified T : Any> Results.toListOfObjects(): List<T> {
    return rows.map { row ->
        val constructor = T::class.java.getDeclaredConstructor()
        val instance = constructor.newInstance()
        T::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            val fieldName = field.name
            val fieldValue = row[fieldName]
            field.set(instance, fieldValue)
        }
        instance
    }
}

fun ResultSet.toResults(): Results {
    val metaData = metaData
    val columnCount = metaData.columnCount
    val columns = (1..columnCount).map { metaData.getColumnLabel(it) }
    val rows = mutableListOf<Map<String, Any?>>()
    while (next()) {
        val row = mutableMapOf<String, Any?>()
        for (i in 1..columnCount) {
            val columnName = metaData.getColumnLabel(i)
            val columnValue = getObject(i)
            row[columnName] = columnValue
        }
        rows.add(row)
    }
    return Results(columns, rows)
}