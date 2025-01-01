package gg.flyte.twilight.data.sql

class QueryBuilder {

    companion object {
        const val ASTERISK = "*"

        data class Condition(val column: String, val condition: String, val value: Any);

        infix fun String.eq(value: Any): Condition {
            if (this.isEmpty()) throw IllegalArgumentException("SQLError: Column cannot be empty!")

            return Condition(this, "=", value)
        }

        infix fun String.gt(value: Any): Condition {
            if (this.isEmpty()) throw IllegalArgumentException("SQLError: Column cannot be empty!")
            return Condition(this, ">", value)
        }

        infix fun String.gte(value: Any): Condition {
            if (this.isEmpty()) throw IllegalArgumentException("SQLError: Column cannot be empty!")
            return Condition(this, ">=", value)
        }

        infix fun String.lt(value: Any): Condition {
            if (this.isEmpty()) throw IllegalArgumentException("SQLError: Column cannot be empty!")
            return Condition(this, "<", value)
        }

        infix fun String.lte(value: Any): Condition {
            if (this.isEmpty()) throw IllegalArgumentException("SQLError: Column cannot be empty!")
            return Condition(this, "<=", value)
        }
    }

    private var operationType: String? = null

    private val selectColumns = mutableListOf<String>()
    private var tableName: String? = null
    private val insertColumns = mutableListOf<String>()
    private val insertValues = mutableListOf<Any?>()
    private val updateSet = mutableMapOf<String, Any?>()
    private var whereClauses = mutableListOf<Condition>()
    private val joinClauses = mutableListOf<String>()
    private var groupByClause: String? = null
    private var havingClause: String? = null
    private var orderByClause: String? = null
    private var limitClause: String? = null

    fun select(vararg columns: String): QueryBuilder {
        operationType = "SELECT"
        selectColumns.addAll(columns)
        return this
    }

    fun update(): QueryBuilder {
        operationType = "UPDATE"
        return this
    }

    fun delete(): QueryBuilder {
        operationType = "DELETE"
        return this
    }

    fun from(table: String): QueryBuilder {
        tableName = table
        return this
    }

    fun table(table: String): QueryBuilder {
        tableName = table
        return this
    }

    fun insertInto(table: String, vararg columns: String): QueryBuilder {
        operationType = "INSERT"
        insertColumns.addAll(columns)
        tableName = table
        return this
    }

    fun values(vararg values: Any?): QueryBuilder {
        if (values.size != insertColumns.size) throw IllegalArgumentException("Mismatched columns and value sizes")
        insertValues.addAll(values)
        return this
    }

    fun set(column: String, value: Any?): QueryBuilder {
        updateSet[column] = value
        return this
    }

    fun leftJoin(table: String, onCondition: String): QueryBuilder {
        joinClauses.add("LEFT JOIN $table ON $onCondition")
        return this
    }

    fun rightJoin(table: String, onCondition: String): QueryBuilder {
        joinClauses.add("RIGHT JOIN $table ON $onCondition")
        return this
    }

    fun innerJoin(table: String, onCondition: String): QueryBuilder {
        joinClauses.add("INNER JOIN $table ON $onCondition")
        return this
    }

    fun where(condition: Condition): QueryBuilder {
        whereClauses.add(condition)
        return this
    }

    fun and(condition: Condition): QueryBuilder {
        whereClauses.add(condition)
        return this
    }

    fun groupBy(vararg columns: String): QueryBuilder {
        groupByClause = columns.joinToString(", ")
        return this
    }

    fun having(condition: String): QueryBuilder {
        havingClause = condition
        return this
    }

    fun orderBy(vararg columns: String): QueryBuilder {
        orderByClause = columns.joinToString(", ")
        return this
    }

    fun limit(limit: Int): QueryBuilder {
        limitClause = limit.toString()
        return this
    }

    fun build(): String {
        return when (operationType) {
            "SELECT" -> buildSelectQuery()
            "INSERT" -> buildInsertQuery()
            "UPDATE" -> buildUpdateQuery()
            "DELETE" -> buildDeleteQuery()
            else -> throw IllegalStateException("Operation type not set or unknown.")
        }
    }

    private fun buildSelectQuery(): String {
        requireNotNull(tableName) { "Table name must be specified for SELECT query." }
        require(selectColumns.isNotEmpty()) { "At least one column must be selected." }
        val columns = selectColumns.joinToString(", ")
        var query = "SELECT $columns FROM $tableName"
        joinClauses.forEach { query += " $it" }
        query += buildWhereClause()
        groupByClause?.let { query += " GROUP BY $it" }
        havingClause?.let { query += " HAVING $it" }
        orderByClause?.let { query += " ORDER BY $it" }
        limitClause?.let { query += " LIMIT $it" }
        return "$query;"
    }

    private fun buildInsertQuery(): String {
        requireNotNull(tableName) { "Table name must be specified for INSERT query." }
        require(insertColumns.isNotEmpty()) { "At least one column must be specified for INSERT query." }
        require(insertValues.isNotEmpty()) { "Values must be provided for INSERT query." }
        val columns = insertColumns.joinToString(", ")
        val placeholders = insertValues.joinToString(", ") { value ->
            if (value is String) "'$value'" else value.toString()
        }
        return "INSERT INTO $tableName ($columns) VALUES ($placeholders);"
    }

    private fun buildUpdateQuery(): String {
        requireNotNull(tableName) { "Table name must be specified for UPDATE query." }
        require(updateSet.isNotEmpty()) { "At least one column must be updated for UPDATE query." }
        val setClause = updateSet.entries.joinToString(", ") { updates ->
            "${updates.key} = " + if (updates.value is String) "'${updates.value}'" else updates.value.toString()
        }
        var query = "UPDATE $tableName SET $setClause"
        query += buildWhereClause()
        return "$query;"
    }

    private fun buildDeleteQuery(): String {
        requireNotNull(tableName) { "Table name must be specified for DELETE query." }
        var query = "DELETE FROM $tableName"
        query += buildWhereClause()
        return "$query;"
    }

    private fun buildWhereClause(): String {
        return buildString {
            if (whereClauses.isNotEmpty()) {
                append(" WHERE ")
                append(whereClauses.joinToString(" AND ") { (column, condition, value) ->
                    if (value is String) {
                        "$column $condition '$value'"
                    } else {
                        "$column $condition $value"
                    }
                })
            }
        }
    }
}