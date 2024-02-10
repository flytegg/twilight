plugins {
    id("gg.flyte.twilight.kotlin")
    id("gg.flyte.twilight.shadow")
}

group = "gg.flyte"
version = "1.0.39"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    testCompileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.mongodb:mongodb-driver-sync:4.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    build { dependsOn(shadowJar) }

    shadowJar {
        minimize()
        val `package` = "gg.flyte.twilight.shaded"
        relocate("kotlin", "$`package`.kotlin")
        relocate("com.mongodb", "$`package`.mongodb")
        relocate("org.bson", "$`package`.bson")
        relocate("org.intellij", "$`package`.intellij")
        relocate("org.jetbrains", "$`package`.jetbrains")
        relocate("io.github.cdimascio.dotenv", "$`package`.dotenv")
        relocate("com.google.gson", "$`package`.gson")
    }
}