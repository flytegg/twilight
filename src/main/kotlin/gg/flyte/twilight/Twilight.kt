package gg.flyte.twilight

import gg.flyte.twilight.environment.Environment
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvBuilder
import org.bukkit.plugin.java.JavaPlugin

class Twilight private constructor(
    javaPlugin: JavaPlugin,
    envBuilder: DotenvBuilder
) {
    init {
        plugin = javaPlugin
        Environment.env(envBuilder.load())
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