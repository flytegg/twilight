plugins {
    id("gg.flyte.twilight.kotlin")
}

group = "gg.flyte"
version = "1.0.39"

repositories {
    mavenLocal()
}

dependencies {
    api(project(":core"))
    compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")
}