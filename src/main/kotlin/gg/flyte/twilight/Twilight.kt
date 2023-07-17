package gg.flyte.twilight

import org.bukkit.plugin.java.JavaPlugin

class Twilight private constructor(
    val plugin: JavaPlugin
) {
    class Builder(private val plugin: JavaPlugin) {
        fun build(): Twilight = Twilight(plugin)
    }
}