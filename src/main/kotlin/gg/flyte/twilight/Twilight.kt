package gg.flyte.twilight

import gg.flyte.twilight.event.CustomEventRegistry
import gg.flyte.twilight.inventory.GUIListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Twilight(javaPlugin: JavaPlugin) {
    lateinit var customEvents: CustomEventRegistry
    var property = ""

    init {
        plugin = javaPlugin
        //Environment.env(envSettings)

        /**
         * Register event listeners
         */

        listOf(
            GUIListener()
        ).forEach { Bukkit.getPluginManager().registerEvents(it, plugin) }
    }

    companion object {
        lateinit var plugin: JavaPlugin
    }

    fun events(init: CustomEventRegistry.() -> Unit): CustomEventRegistry {
        val customEvents = CustomEventRegistry.apply(init)
        this.customEvents = customEvents
        return customEvents
    }

    /*class Builder(private val plugin: JavaPlugin) {
        private var envBuilder = Environment.Builder()

        fun env(envBuilder: Environment.Builder.() -> Unit) = apply {
            this.envBuilder.envBuilder()
        }

        fun build() = Twilight(plugin, envBuilder.build())
    }*/

}

fun twilight(plugin: JavaPlugin, init: Twilight.() -> Unit): Twilight = Twilight(plugin).apply(init)