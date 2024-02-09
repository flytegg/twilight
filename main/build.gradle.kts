plugins {
    id("gg.flyte.twilight.kotlin")
    id("maven-publish")
    id("com.github.johnrengelman.shadow")
}

group = "gg.flyte"
version = "1.0.39"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
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

    javadoc { options.encoding = Charsets.UTF_8.name() }
    processResources { filteringCharset = Charsets.UTF_8.name() }
}

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


