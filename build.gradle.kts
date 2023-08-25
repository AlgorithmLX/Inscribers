import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.patcher.tasks.ReobfuscateJar
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.tasks.JarJar
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.spongepowered.asm.gradle.plugins.MixinExtension

buildscript {
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    eclipse
    idea
    scala
    id("com.github.johnrengelman.shadow") version "8.+"
    id("net.minecraftforge.gradle") version "6.+"
}

apply(plugin = "org.spongepowered.mixin")

jarJar.enable()

val mod_version: String by project
val mod_group_id: String by project
val minecraft_version: String by project
val forge_version: String by project
val main = sourceSets["main"]

group = mod_group_id
version = mod_version

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

configurations {
    implementation.get().extendsFrom(this["shadow"])
}

configure<UserDevExtension> {
    mappings("official", minecraft_version)

    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    copyIdeResources.set(true)

    runs {
        create("client") {
            workingDirectory (project.file("run"))

            jvmArg("-XX:+AllowEnhancedClassRedefinition")
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "AncientMagic")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")
            arg("-mixin.config=inscribers.mixins.json")

            mods {
                create("inscribers") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }

        create("server") {
            workingDirectory (project.file("run"))

            jvmArg("-XX:+AllowEnhancedClassRedefinition")
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "AncientMagic")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")
            arg("-mixin.config=inscribers.mixins.json")

            mods {
                create("inscribers") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }

        create("data") {
            workingDirectory (project.file("run"))

            jvmArg("-XX:+AllowEnhancedClassRedefinition")
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            args("--mod", "inscribers", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))

            mods {
                create("inscribers") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }
    }
}

configure<MixinExtension> {
    add(main, "inscribers.refmap.json")
    config("inscribers.mixins.json")
}

repositories {
    mavenCentral()
    maven("https://maven.tterrag.com/")
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

    val shadow = configurations["shadow"]

    implementation(fg.deobf("com.tterrag.registrate:Registrate:MC1.16.5-1.0.10"))
    jarJar(group = "com.tterrag.registrate", name = "Registrate", version = "[MC1.16.5,MC1.17)")

    shadow("org.scala-lang:scala-library:2.13.11")
    shadow("org.scala-lang:scala-reflect:2.13.11")

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks {
    getByName("build").dependsOn("shadowJar")
    withType<Jar> {
        from(main.output)
        manifest {
            attributes(
                mapOf(
                    "Specification-Title" to "inscribers",
                    "Specification-Vendor" to "AlgorithmLX",
                    "Specification-Version" to "1",
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to version,
                    "Implementation-Timestamp" to ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
                    "MixinConfigs" to "ancient.mixins.json"
                )
            )
        }
        finalizedBy("reobfJar")
    }

    getByName<ShadowJar>("shadowJar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        configurations = listOf(project.configurations.getByName("shadow"))

        archiveClassifier.set("")

        mergeServiceFiles()
        exclude("**/module-info.class")
    }

    withType<ReobfuscateJar> {
        jarJar
    }

    withType<JarJar> {
        finalizedBy("reobfJarJar")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
