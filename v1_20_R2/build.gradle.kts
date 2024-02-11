plugins {
    id("gg.flyte.twilight.kotlin")
}

repositories {
    mavenLocal()
}

dependencies {
    api(project(":core"))
    compileOnly("org.spigotmc:spigot:1.20.2-R0.1-SNAPSHOT")
}