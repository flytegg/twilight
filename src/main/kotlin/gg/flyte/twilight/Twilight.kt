package gg.flyte.twilight

import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.inventory.GUIListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Twilight private constructor(
    javaPlugin: JavaPlugin,
    envSettings: Environment.Settings
) {
    init {
        plugin = javaPlugin
        Environment.env(envSettings)

        /*
        Register event listeners
         */
        listOf(
            GUIListener()
        ).forEach { Bukkit.getPluginManager().registerEvents(it, plugin) }
    }

    companion object {
        lateinit var plugin: JavaPlugin
    }

    class Builder(private val plugin: JavaPlugin) {
        private var envBuilder = Environment.Builder()

        fun env(envBuilder: Environment.Builder.() -> Unit) = apply {
            this.envBuilder.envBuilder()
        }

        fun build() = Twilight(plugin, envBuilder.build())
    }
}