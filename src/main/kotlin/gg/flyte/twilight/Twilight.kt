package gg.flyte.twilight

import gg.flyte.twilight.environment.Environment
import gg.flyte.twilight.inventory.GUIListener
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvBuilder
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Twilight private constructor(
    javaPlugin: JavaPlugin,
    envBuilder: DotenvBuilder
) {
    init {
        plugin = javaPlugin
        Environment.env(envBuilder.load())
        Bukkit.getPluginManager().registerEvents(GUIListener(), plugin)
    }

    companion object {
        lateinit var plugin: JavaPlugin
    }

    class Builder(private val plugin: JavaPlugin) {
        private val envBuilder = Dotenv.configure()

        fun env(fileName: String) = apply {
            envBuilder.filename(fileName)
        }

        fun build(): Twilight = Twilight(plugin, envBuilder)
    }
}