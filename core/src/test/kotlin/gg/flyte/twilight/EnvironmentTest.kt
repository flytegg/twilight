package gg.flyte.twilight

import gg.flyte.twilight.environment.Environment

fun main() {
    envTest()
}

fun envTest() {
    env {
        useDifferentEnvironments = true
    }

    println("Environment.isDev() = ${Environment.isDev()}")
    println("Environment.isProd() = ${Environment.isProd()}")
    println("Test output: ${Environment.get("TEST")}")
}

private fun env(init: Environment.Settings.() -> Unit) {
    Environment.env(Environment.Settings().apply(init))
}