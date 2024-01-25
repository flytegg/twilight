package gg.flyte.twilight

import org.bukkit.plugin.java.JavaPlugin

fun main() {
    useBuilderTest()
}

lateinit var plugin: JavaPlugin

fun useBuilderTest() {
    val twilight = twilight(plugin) {
        env {
            useDifferentEnvironments = true
            devEnvFileName = ".env.dev"
            prodEnvFileName = ".env.prod"
        }
    }

    twilight(plugin) {
        env()
        mongo()
        nameCache()
        network {
            server {
                name = "Yippie"
            }
        }
    }
}

