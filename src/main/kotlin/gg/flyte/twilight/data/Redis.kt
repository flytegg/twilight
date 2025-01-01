package gg.flyte.twilight.data

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.environment.Environment
import redis.clients.jedis.DefaultJedisClientConfig
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Redis {
    private lateinit var jedis: Jedis
    private lateinit var settings: Settings
    private val executor: Executor = Executors.newCachedThreadPool()
    fun redis(redis: Settings) {
        jedis = getClient(redis)
        settings = redis
    }

    private fun getClient(redis: Settings): Jedis = when (redis.authentication){
        Authentication.NONE -> Jedis(redis.host, redis.port)
        Authentication.USERNAME_PASSWORD -> Jedis(redis.host, redis.port, DefaultJedisClientConfig.builder().user(redis.username).password(redis.password).timeoutMillis(redis.timeout).build())
        Authentication.URL -> Jedis(redis.url)
    }

    private fun publishSync(channel: String, message: String) = jedis.publish(channel, message)
    fun publish(channel: String, message: String): CompletableFuture<Long> = CompletableFuture.supplyAsync({ publishSync(channel, message) }, executor)
    private fun setSync(key: String, value: String) = jedis.set(key, value)
    fun set(key: String, value: String): CompletableFuture<String> = CompletableFuture.supplyAsync({ setSync(key, value) }, executor)
    private fun getSync(key: String) = jedis.get(key)
    fun get(key: String): CompletableFuture<String> = CompletableFuture.supplyAsync({ getSync(key) }, executor)
    private fun deleteSync(key: String) = jedis.del(key)
    fun delete(key: String): CompletableFuture<Long> = CompletableFuture.supplyAsync({ deleteSync(key) }, executor)

    fun addListener(listener: TwilightRedisListener): TwilightRedisListener {
        val client = getClient(settings)
        client.subscribe(listener, listener.channel)
        client.close()
        return listener
    }
    fun addListener(channel: String, block: RedisMessage.() -> Unit): TwilightRedisListener {
        val client = getClient(settings)
        val listener = RedisListener(channel, block)
        client.subscribe(listener, channel)
        client.close()
        return listener
    }
    class Settings {
        var authentication = if (Twilight.usingEnv) Authentication.fromString(Environment.get("REDIS_AUTHENTICATION")) else Authentication.NONE
        var host: String = if (Twilight.usingEnv) Environment.get("REDIS_HOST") else "localhost"
        var port: Int = if (Twilight.usingEnv) Environment.get("REDIS_PORT").toInt() else 6379
        var timeout: Int = if (Twilight.usingEnv) Environment.get("REDIS_TIMEOUT").toInt() else 0
        val username: String = if (Twilight.usingEnv && authentication == Authentication.USERNAME_PASSWORD) Environment.get("REDIS_USERNAME") else ""
        var password: String = if (Twilight.usingEnv && authentication == Authentication.USERNAME_PASSWORD) Environment.get("REDIS_PASSWORD") else ""
        var url: String = if (Twilight.usingEnv && authentication == Authentication.URL) Environment.get("REDIS_URL") else ""
    }

    enum class Authentication {
        NONE,
        USERNAME_PASSWORD,
        URL;

        companion object {
            fun fromString(value: String): Authentication = when (value.uppercase()) {
                "NONE" -> NONE
                "USERNAME_PASSWORD" -> USERNAME_PASSWORD
                "URL" -> URL
                else -> throw IllegalArgumentException("Invalid authentication type: $value")
            }
        }

    }

}

data class RedisMessage(val channel: String, val message: String, val listener: TwilightRedisListener)

abstract class TwilightRedisListener(val channel: String) : JedisPubSub() {
    override fun onMessage(channel: String?, message: String?) {
        channel ?: return
        message?: return
        if (channel == this.channel) onMessage(message)
    }
    abstract fun onMessage(message: String)
    fun unregister() = unsubscribe()
}

class RedisListener(channel: String, val block: RedisMessage.() -> Unit): TwilightRedisListener(channel) {
    override fun onMessage(message: String) {
        block(RedisMessage(channel, message, this))
    }
}