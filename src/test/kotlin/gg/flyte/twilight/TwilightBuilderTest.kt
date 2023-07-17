package gg.flyte.twilight

import org.bukkit.plugin.java.JavaPlugin

fun main() {
    useBuilderTest()
}

lateinit var plugin: JavaPlugin

fun useBuilderTest() {
    Twilight.Builder(plugin)
}