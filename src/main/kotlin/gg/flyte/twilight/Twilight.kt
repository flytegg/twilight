package gg.flyte.twilight

import gg.flyte.twilight.data.MongoDB
import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.inventory.GUIListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Twilight(javaPlugin: JavaPlugin) {

    init {
        plugin = javaPlugin

        listOf(
            GUIListener()
        ).forEach { Bukkit.getPluginManager().registerEvents(it, plugin) }
    }

    companion object {
        lateinit var plugin: JavaPlugin
    }

    fun env(init: Environment.Settings.() -> Unit) {
        Environment.env(Environment.Settings().apply(init))
    }

    fun mongo(init: MongoDB.Settings.() -> Unit) {
        MongoDB.mongo(MongoDB.Settings().apply(init))
    }

}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit): Twilight = Twilight(plugin).apply(init)
