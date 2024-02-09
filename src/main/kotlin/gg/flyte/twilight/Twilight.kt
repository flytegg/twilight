package gg.flyte.twilight

import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.service.NameCacheService
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.event.custom.chat.command.ChatClickCommand
import gg.flyte.twilight.event.customEventListeners
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.itembuilder.ItemBuilder
import gg.flyte.twilight.nms.NMS
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class Twilight(javaPlugin: JavaPlugin) {

    init {
        plugin = javaPlugin
        server = javaPlugin.server
        run {
            customEventListeners
            ItemBuilder.Companion
            ChatClickCommand.register()
            NMS
        }
    }

    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var server: Server
        var usingEnv = false
        val internalPdc by lazy { "_twilight_${plugin.name.lowercase()}" }
    }

    fun env(init: Environment.Settings.() -> Unit = {}) {
        usingEnv = true
        Environment.env(Environment.Settings().apply(init))
    }

    fun mongo(init: MongoDB.Settings.() -> Unit = {}) = MongoDB.mongo(MongoDB.Settings().apply(init))

    fun nameCache(init: NameCacheService.Settings.() -> Unit = {}) = NameCacheService.nameCache(NameCacheService.Settings().apply(init))

    fun terminate() = customEventListeners.applyForEach { unregister() }
}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit = {}): Twilight = Twilight(plugin).apply(init)
