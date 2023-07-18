package gg.flyte.twilight.environment

import io.github.cdimascio.dotenv.Dotenv

object Environment {

    private lateinit var dotenv: Dotenv

    fun env(env: Settings) {
        if (env.useDifferentEnvironments) {
            TODO("Implement dev vs prod env setup")
        }

        TODO("Implement other settings stuff")
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

    class Settings {
        var useDifferentEnvironments: Boolean = false
        // TODO: Implement other settings stuff
    }

}