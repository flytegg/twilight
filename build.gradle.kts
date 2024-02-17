plugins {
    kotlin("jvm") version "1.8.21"
    id("maven-publish")
}

group = "gg.flyte"
version = "1.1.8-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    api("io.github.cdimascio:dotenv-kotlin:6.4.1")
    api("org.mongodb:mongodb-driver-kotlin-sync:4.11.0")
    api("com.google.code.gson:gson:2.10.1")

    implementation(kotlin("reflect"))

//    api("com.github.okkero:Skedule:v1.2.6")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc { options.encoding = Charsets.UTF_8.name() }
    processResources { filteringCharset = Charsets.UTF_8.name() }
}

kotlin { jvmToolchain(17) }

publishing {
    repositories {
        maven {
            name = "flyte-repository"
            url = uri(
                "https://repo.flyte.gg/${
                    if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"
                }"
            )
            credentials {
                username = System.getenv("MAVEN_NAME") ?: property("mavenUser").toString()
                password = System.getenv("MAVEN_SECRET") ?: property("mavenPassword").toString()
            }
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = group.toString()
                artifactId = "twilight"
                version = version.toString()

                from(components["java"])
            }
        }
    }
}


