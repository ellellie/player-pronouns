plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
    `maven-publish`
}

version = "2.3.0-${stonecutter.current.version}"
group = "dev.ashhhleyyy"

base {
    archivesName = "player-pronouns"
}

repositories {
    // needed for placeholder-api
    maven {
        name = "NucleoidMC"
        url = uri("https://maven.nucleoid.xyz/")
    }
    // permissions api
    maven {
        name = "Sonatype OSS"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    // Minecraft
    minecraft("com.mojang:minecraft:${stonecutter.current.version}")
    mappings("net.fabricmc:yarn:${mod.dep("yarn_mappings")}:v2")

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api")}")

    // placeholder-api
    modImplementation("eu.pb4:placeholder-api:${mod.dep("placeholder_api")}")
    include("eu.pb4:placeholder-api:${mod.dep("placeholder_api")}")

    // player-data-api
    modImplementation("eu.pb4:player-data-api:${mod.dep("player_data_api")}")
    include("eu.pb4:player-data-api:${mod.dep("player_data_api")}")

    // fabric-api-permissions
    modImplementation("me.lucko:fabric-permissions-api:${mod.dep("fabric_permissions_api")}")
    include("me.lucko:fabric-permissions-api:${mod.dep("fabric_permissions_api")}")
}

loom {
    runtimeOnlyLog4j.set(true)
}

tasks.processResources {
    properties(listOf("fabric.mod.json"),
        "version" to project.version,
        "loader_version" to mod.dep("fabric_loader"),
        "minecraft_version" to stonecutter.current.version,
        "fabric_api_version" to mod.dep("fabric_api"),
        "placeholder_api_version" to mod.dep("placeholder_api"),
        "player_data_api_version" to mod.dep("player_data_api"),
        "fabric_permissions_api_version" to mod.dep("fabric_permissions_api")
    )
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs"))
    dependsOn("build")
}

modrinth {
    projectId.set("player-pronouns")
    uploadFile.set(tasks.remapJar.get())
    dependencies {
        required.project("fabric-api")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }

    repositories {
        if (System.getenv("MAVEN_URL") != null) {
            maven {
                name = "ashhhleyyy"
                setUrl(System.getenv("MAVEN_URL"))
                credentials {
                    username = System.getenv("MAVEN_USERNAME")
                    password = System.getenv("MAVEN_PASSWORD")
                }
            }
        } else {
            mavenLocal()
        }
    }
}
