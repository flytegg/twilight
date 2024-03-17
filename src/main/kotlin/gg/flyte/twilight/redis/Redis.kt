package gg.flyte.twilight.redis

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.environment.Environment
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Redis {
    private lateinit var jedis: Jedis
    private val executor: Executor = Executors.newCachedThreadPool()

    fun redis(redis: Settings){
        jedis = Jedis(redis.host, redis.port, redis.timeout)
    }

    fun publishSync(channel: String, message: String){
        jedis.publish(channel, message)
    }

    fun publish(channel: String, message: String) = CompletableFuture.supplyAsync({ publishSync(channel, message) }, executor)

    class Settings {
        var host: String = if (Twilight.usingEnv) Environment.get("REDIS_HOST") else "localhost"
        var port: Int = if (Twilight.usingEnv) Environment.get("REDIS_PORT").toInt() else 6379
        var timeout: Int = if (Twilight.usingEnv) Environment.get("REDIS_TIMEOUT").toInt() else 0
    }

    fun addListener(listener: TwilightRedisListener) = jedis.subscribe(listener, listener.channel)
    fun addListener(channel: String, block: RedisMessage.() -> Unit) = jedis.subscribe(RedisListener(channel, block), channel)

}

data class RedisMessage(val channel: String, val message: String)

abstract class TwilightRedisListener(val channel: String) : JedisPubSub() {

    override fun onMessage(channel: String?, message: String?) {
        channel ?: return
        message?: return
        if (channel == this.channel) onMessage(message)
    }

    abstract fun onMessage(message: String)

}

class RedisListener(channel: String, val block: RedisMessage.() -> Unit): TwilightRedisListener(channel) {

    override fun onMessage(message: String) {
        block(RedisMessage(channel, message))
    }

}




