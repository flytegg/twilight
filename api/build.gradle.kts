plugins {
    id("gg.flyte.twilight.kotlin")
    id("gg.flyte.twilight.shadow")
    id("maven-publish")
}

group = "gg.flyte"
version = "1.0.39"

dependencies {
    implementation(project(":core"))
    implementation(project(":v1_20_R1"))
    implementation(project(":v1_20_R2"))
    implementation(project(":v1_20_R3"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName.set("twilight-$version.jar")
    }

    /*register("renameJar") {
        group = "build"
        description = "Renames the jar to twilight-<version>.jar"
        dependsOn(shadowJar)
        doLast {
            with(shadowJar.get()) {
                archiveFileName.set("twilight-$version.jar")
            }
        }
    }*/
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