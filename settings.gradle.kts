plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "twilight"
include(
    "api",
    "core",
    "v1_20_R1",
    "v1_20_R2",
    "v1_20_R3",
)