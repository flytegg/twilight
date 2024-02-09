package gg.flyte.twilight.environment

import io.github.cdimascio.dotenv.Dotenv

object Environment {

    private var dotenv: Dotenv = Dotenv.load()

    fun env(env: Settings) {
        val envBuilder = Dotenv.configure()
        if (env.useDifferentEnvironments) {
            envBuilder.filename(
                if (Dotenv.configure().load().get("ENVIRONMENT", "PROD") == "DEV") env.devEnvFileName
                else env.prodEnvFileName
            )
        }
        dotenv = envBuilder.load()
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
        var useDifferentEnvironments: Boolean = true
        var prodEnvFileName: String = ".env.prod"
        var devEnvFileName: String = ".env.dev"
        // TODO: Implement other settings stuff
    }

}