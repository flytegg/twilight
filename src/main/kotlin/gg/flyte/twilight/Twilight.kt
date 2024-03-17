package gg.flyte.twilight

import gg.flyte.twilight.builders.item.ItemBuilder
import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.service.NameCacheService
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.event.custom.chat.command.ChatClickCommand
import gg.flyte.twilight.event.customEventListeners
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.redis.Redis
import gg.flyte.twilight.server.ServerSoftware
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class Twilight(javaPlugin: JavaPlugin) {

    init {
        plugin = javaPlugin
        run {
            ServerSoftware
            customEventListeners
            ItemBuilder.Companion
            ChatClickCommand.register()
        }
    }

    companion object {
        lateinit var plugin: JavaPlugin
        var usingEnv = false
        val internalPdc by lazy { "_twilight_${plugin.name.lowercase()}" }

        fun isFolia(): Boolean = ServerSoftware.isFolia()
        fun isPaper(): Boolean = ServerSoftware.isPaper()
        fun isSpigot(): Boolean = ServerSoftware.isSpigot()
        fun isCraftBukkit(): Boolean = ServerSoftware.isCraftBukkit()
    }

    fun env(init: Environment.Settings.() -> Unit = {}) {
        usingEnv = true
        Environment.env(Environment.Settings().apply(init))
    }

    fun mongo(init: MongoDB.Settings.() -> Unit = {}) = MongoDB.mongo(MongoDB.Settings().apply(init))

    fun redis(init: Redis.Settings.() -> Unit = {}) = Redis.redis(Redis.Settings().apply(init))

    fun nameCache(init: NameCacheService.Settings.() -> Unit = {}) = NameCacheService.nameCache(NameCacheService.Settings().apply(init))

    fun terminate() = customEventListeners.applyForEach { unregister() }
}

fun hello(){
    val builder = ItemBuilder(Material.DIAMOND_SWORD)
}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit = {}): Twilight = Twilight(plugin).apply(init)
