package gg.flyte.twilight.data.sql

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class SQLWrapper(private val url: String, private val user: String, private val password: String) {
    private var connection: Connection? = null

    fun connect() {
        try {
            connection = DriverManager.getConnection(url, user, password)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun executeQuery(query: String): Results? {
        var result: Results? = null
        try {
            val statement: Statement? = connection?.createStatement()
            val resultSet = statement?.executeQuery(query)
            result = resultSet?.toResults()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result;
    }

    fun execute(sql: String): Boolean {
        var result = false
        try {
            val statement: Statement? = connection?.createStatement()
            statement?.execute(sql)
            result=true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result;
    }

    fun executeUpdate(query: String): Int {
        var rowsAffected = 0
        try {
            val statement: Statement? = connection?.createStatement()
            rowsAffected = statement?.executeUpdate(query) ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rowsAffected
    }

    fun close() {
        try {
            connection?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}