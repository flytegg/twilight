package gg.flyte.twilight.environment

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvBuilder

object Environment {

    private lateinit var dotenv: Dotenv

    fun env(env: Settings) {
        if (env.useDifferentEnvironments) {
            println("YAY")
        } else {
            println("awwww")
        }
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

    class Builder(
        var envBuilder: DotenvBuilder? = Dotenv.configure()
    ) {
        var useDifferentEnvironments = false

        fun build() = Settings(useDifferentEnvironments)
    }

    class Settings(
        val useDifferentEnvironments: Boolean
    )

}