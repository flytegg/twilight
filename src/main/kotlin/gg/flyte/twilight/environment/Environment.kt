package gg.flyte.twilight.environment

import io.github.cdimascio.dotenv.Dotenv

object Environment {
    private lateinit var dotenv: Dotenv

    fun env(dotenv: Dotenv) {
        this.dotenv = dotenv
    }

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