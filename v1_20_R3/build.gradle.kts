plugins {
    id("gg.flyte.twilight.kotlin")
}

repositories {
    mavenLocal()
}

dependencies {
    api(project(":main"))
    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT")
}