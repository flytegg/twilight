package gg.flyte.twilight.data.sql

import java.sql.Connection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Statement

class AsyncSQLWrapper(private val url: String, private val user: String, private val password: String) {

    private var connection: Connection? = null

    suspend fun connect() {
        coroutineScope {
            connection = DriverManager.getConnection(url, user, password)
        }
    }

    suspend fun executeQuery(query: String): Results? {
        return coroutineScope {
            val resultSet = async(Dispatchers.IO) {
                val statement: Statement? = connection?.createStatement()
                statement?.executeQuery(query)
            }
            val rs = resultSet.await();
            rs?.toResults()
        }
    }

    suspend fun execute(sql: String): Boolean {
        return coroutineScope {
            var result = false
            try {
                val statement: Statement? = connection?.createStatement()
                statement?.execute(sql)
                result=true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result;
        }
    }

    suspend fun executeUpdate(query: String): Int {
        return coroutineScope {
            val rowsAffected = async(Dispatchers.IO) {
                val statement: PreparedStatement? = connection?.prepareStatement(query);
                statement?.executeUpdate() ?: 0
            }
            rowsAffected.await()
        }
    }

    suspend fun close() {
        coroutineScope {
            connection?.close()
        }
    }
}