package gg.flyte.twilight

import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.event.CustomEventRegistry
import gg.flyte.twilight.inventory.GUIListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Twilight(javaPlugin: JavaPlugin) {

    private lateinit var customEvents: CustomEventRegistry

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

    fun events(init: CustomEventRegistry.() -> Unit) {
        this.customEvents = CustomEventRegistry.apply(init)
    }

}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit): Twilight = Twilight(plugin).apply(init)
