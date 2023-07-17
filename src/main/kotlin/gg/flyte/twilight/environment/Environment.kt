package gg.flyte.twilight.environment

import io.github.cdimascio.dotenv.Dotenv

object Environment {
    private val dotenv = Dotenv.load()

    fun isDev(): Boolean {
        return get("ENVIRONMENT") == "DEV"
    }

    fun isProd(): Boolean {
        return get("ENVIRONMENT") == "PROD"
    }

    fun get(variable: String): String {
        return dotenv.get(variable)
    }
}