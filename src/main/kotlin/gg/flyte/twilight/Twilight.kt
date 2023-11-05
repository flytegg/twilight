package gg.flyte.twilight

import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.data.service.NameCacheService
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.event.custom.interact.listener.InteractEventListener
import gg.flyte.twilight.event.customEvents
import gg.flyte.twilight.extension.applyForEach
import gg.flyte.twilight.inventory.GUIListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Twilight(javaPlugin: JavaPlugin) {

    init {
        plugin = javaPlugin
        run { customEvents }
    }

    companion object {
        lateinit var plugin: JavaPlugin
        var usingEnv = false
    }

    fun env(init: Environment.Settings.() -> Unit) {
        usingEnv = true
        Environment.env(Environment.Settings().apply(init))
    }

    fun mongo(init: MongoDB.Settings.() -> Unit) = MongoDB.mongo(MongoDB.Settings().apply(init))

    fun nameCache(init: NameCacheService.Settings.() -> Unit) = NameCacheService.nameCache(NameCacheService.Settings().apply(init))

    fun terminate() = customEvents.applyForEach { unregister() }

}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit): Twilight = Twilight(plugin).apply(init)
