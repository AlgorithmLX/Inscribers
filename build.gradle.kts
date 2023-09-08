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
    id("net.minecraftforge.gradle") version "6.+"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
}

apply(plugin = "org.spongepowered.mixin")

evaluationDependsOnChildren()

val mod_version: String by project
val mod_group_id: String by project
val minecraft_version: String by project
val forge_version: String by project

val shadowX: Configuration by configurations.creating
val main = sourceSets["main"]

jarJar.enable()

group = mod_group_id
version = mod_version

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

configurations {
    minecraftLibrary { extendsFrom(shadowX) }
}

configure<UserDevExtension> {
    val mappingsChannel: String by project
    val parchmentVersion: String? by project
    if (mappingsChannel == "official") mappings(mappingsChannel, minecraft_version)
    else mappings(mappingsChannel, "${parchmentVersion!!}-${minecraft_version}")

    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    copyIdeResources.set(true)

    runs {
        create("client") {
            workingDirectory (project.file("run"))

            jvmArg("-XX:+AllowEnhancedClassRedefinition")
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "Inscribers")
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
            property("forge.enabledGameTestNamespaces", "Inscribers")
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
    maven("https://maven.blamejared.com")
    maven("https://maven.tterrag.com/")
    maven("https://modmaven.dev")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.architectury.dev/")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

    val registrate_version: String by project
    val registrate_range: String by project
    val scala_version: String by project
    val jei_version: String by project
    val craftTweakerVersion: String by project

    compileOnly(fg.deobf("com.tterrag.registrate:Registrate:MC${minecraft_version}-${registrate_version}"))
    jarJar(fg.deobf("com.tterrag.registrate:Registrate:MC${minecraft_version}-${registrate_version}")) {
        jarJar.ranged(this, "[MC${minecraft_version},MC${registrate_range})")
    }

    shadowX("org.scala-lang:scala-library:${scala_version}")
    shadowX("org.scala-lang:scala-reflect:${scala_version}")

    compileOnly(fg.deobf("com.blamejared.crafttweaker:CraftTweaker-${minecraft_version}:${craftTweakerVersion}"))
    compileOnly(fg.deobf("mezz.jei:jei-${minecraft_version}:${jei_version}:api"))
    compileOnly(fg.deobf("software.bernie.geckolib:geckolib-forge-1.16.5:3.0.106"))
    runtimeOnly(fg.deobf("software.bernie.geckolib:geckolib-forge-1.16.5:3.0.106"))

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks {
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
                    "MixinConfigs" to "inscribers.mixins.json"
                )
            )
        }
        finalizedBy("reobfJar")
    }

    withType<ReobfuscateJar> {
        jarJar
    }

    withType<JarJar> {
        from(provider { shadowX.map(::zipTree).toTypedArray() })
        finalizedBy("reobfJarJar")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
