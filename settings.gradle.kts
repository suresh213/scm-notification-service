pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from(files("versions.toml"))
        }
    }
}
include("app")
rootProject.name = "notification-service"