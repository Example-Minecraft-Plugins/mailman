import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "me.davipccunha.tests"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("net.md-5:bungeecord-chat:1.8-SNAPSHOT")
    implementation(project(":api"))
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveFileName.set("mailman.jar")

    destinationDirectory.set(file("D:\\Local Minecraft Server\\plugins"))
}

bukkit {
    name = "mailman"
    version = "${project.version}"
    main = "me.davipccunha.tests.mailman.MailmanPlugin"
    description = "Plugin that allows players to have a inventory that admins can add items to for players to collect."
    author = "davipccunha"
    prefix = "Mailman" // As shown in console
    apiVersion = "1.8"
    depend = listOf("bukkit-utils")

    commands {
        register("correio") {
            description = "Opens up the mailman GUI."
            permission = "mailman.command"
        }
    }
}