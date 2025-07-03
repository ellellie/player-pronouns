pluginManagement {
    repositories {
        maven {
            name = "FabricMC"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Cotton"
            url = uri("https://server.bbkr.space/artifactory/libs-release")
        }
        maven("https://maven.kikugie.dev/releases") { name = "Kikugie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "Kikugie Snapshots" }
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7-beta.4"
}

rootProject.name = "player-pronouns"

stonecutter {
    create(rootProject) {
        versions("1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7")
    }
}

//dependencyResolutionManagement {
//    versionCatalogs {
//        create("libs") {
//            from(files("libs.versions.toml"))
//        }
//    }
//}
