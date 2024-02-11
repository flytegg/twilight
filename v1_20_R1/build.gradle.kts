plugins {
    id("gg.flyte.twilight.kotlin")
}

repositories {
    mavenLocal()
}

dependencies {
    api(project(":core"))
    compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")
}