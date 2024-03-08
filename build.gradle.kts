/*
 *   SimpleToggleSprint
 *   Copyright (C) 2021  My-Name-Is-Jeff
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.blossom") version "2.1.0"
    id("io.github.juuxel.loom-quiltflower") version "1.10.0"
    id("gg.essential.loom") version "1.3.12"
    id("gg.essential.defaults") version "0.3.0"
    idea
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
}

version = "2.3.0"
group = "mynameisjeff.simpletogglesprint"

quiltflower {
    quiltflowerVersion.set("1.9.0")
}

loom {
    silentMojangMappingsLicense()
    runConfigs {
        getByName("client") {
            isIdeConfigGenerated = true
            property("elementa.dev", "true")
            property("elementa.debug", "true")
            property("elementa.invalid_usage", "warn")
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
            property("fml.debugAccessTransformer", "true")
            property("fml.remappingDebug", "true")
            property("fml.remappingDebug.dumpFieldMaps", "true")
            property("fml.remappingDebug.dumpMethodMaps", "true")
            property("fml.coreMods.load", "mynameisjeff.simpletogglesprint.tweaker.FMLLoadingPlugin")
            programArgs("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            programArgs("--mixin", "mixins.simpletogglesprint.json")
        }
        remove(getByName("server"))
    }
    forge {
        mixinConfig("mixins.simpletogglesprint.json")
        accessTransformer("src/main/resources/META-INF/accesstransformer.cfg")
    }
    mixin {
        defaultRefmapName = "mixins.simpletogglesprint.refmap.json"
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
    maven("https://jitpack.io")
}

val shadowMe: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    shadowMe(annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")!!)
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly("org.spongepowered:mixin:0.8.5")


    shadowMe("gg.essential:loader-launchwrapper:1.2.2")
    implementation("gg.essential:essential-1.8.9-forge:14616+g169bd9af6a")
}

sourceSets {
    main {
        output.setResourcesDir(layout.buildDirectory.file("/classes/kotlin/main"))
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
            resources {
                property("version", project.version.toString())
                property("mcversion", "1.8.9")
            }
        }
    }

}

tasks {
    processResources {
        dependsOn(compileJava)
    }
    named<Jar>("jar") {
        archiveBaseName.set("SimpleToggleSprint")

        manifest {
            attributes(
                mapOf(
                    "FMLAT" to "accesstransformer.cfg",
                    "FMLCorePlugin" to "mynameisjeff.simpletogglesprint.tweaker.FMLLoadingPlugin",
                    "FMLCorePluginContainsFMLMod" to true,
                    "ForceLoadAsMod" to true,
                    "MixinConfigs" to "mixins.simpletogglesprint.json",
                    "ModSide" to "CLIENT",
                    "ModType" to "FML",
                    "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                    "TweakOrder" to "0"
                )
            )
        }

        enabled = false
    }
    named<ShadowJar>("shadowJar") {
        archiveFileName.set(jar.get().archiveFileName)
        archiveClassifier.set("dev")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(shadowMe)

        relocate("com.llamalad7.mixinextras", "mynameisjeff.simpletogglesprint.mixinextras")

        exclude(
            "**/LICENSE.md",
            "**/LICENSE.txt",
            "**/LICENSE",
            "**/NOTICE",
            "**/NOTICE.txt",
            "pack.mcmeta",
            "dummyThing",
            "**/module-info.class",
            "META-INF/proguard/**",
            "META-INF/maven/**",
            "META-INF/versions/**",
            "META-INF/com.android.tools/**",
            "fabric.mod.json"
        )
        mergeServiceFiles()
    }
    named<RemapJarTask>("remapJar") {
        inputFile.set(shadowJar.get().archiveFile)
    }
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xbackend-threads=0")
        }
        kotlinDaemonJvmArguments.set(listOf("-Xmx2G"))
    }
}

kotlin {
    jvmToolchain(8)
}
