package com.algorithmlx.inscribers.api

import com.algorithmlx.inscribers.ModId
import net.minecraft.crash.CrashReport
import net.minecraft.util.text.*
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.thread.SidedThreadGroups
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.loading.FMLEnvironment

val context: ModLoadingContext = ModLoadingContext.get()

val modBus: IEventBus = FMLJavaModLoadingContext.get().modEventBus
val forgeBus: IEventBus = MinecraftForge.EVENT_BUS

fun isLogicalClient(level: World): Boolean = level.isClientSide

fun isLogicalServer(level: World): Boolean = !level.isClientSide

fun isLogicalClient(): Boolean = Thread.currentThread().threadGroup == SidedThreadGroups.CLIENT

fun isLogicalServer(): Boolean = Thread.currentThread().threadGroup == SidedThreadGroups.SERVER

fun isPhysicalClient(): Boolean = FMLEnvironment.dist.isClient

fun isPhysicalServer(): Boolean = FMLEnvironment.dist.isDedicatedServer

fun getPhysicalClient(): Dist = Dist.CLIENT

fun getPhysicalServer(): Dist = Dist.DEDICATED_SERVER

fun menu(context: String, vararg objects: Any): ITextComponent = translate("menu", context, objects)

fun translate(id: String, context: String, vararg objects: Any): ITextComponent = TranslationTextComponent(basedText(id, context), *objects)

fun keybind(id: String, context: String): ITextComponent = KeybindTextComponent(basedText(id, context))

fun scoreText(id: String, context: String, objective: String): ITextComponent = ScoreTextComponent(basedText(id, context), objective)

fun selectorText(id: String, context: String): ITextComponent = SelectorTextComponent(basedText(id, context))

fun stringText(id: String, context: String): ITextComponent = StringTextComponent(basedText(id, context))

fun basedText(id: String, context: String): String = "$id.${ModId}.$context"

fun modLoad(id: String): Boolean = ModList.get().isLoaded(id)

fun makeConfig(modId: String = ModId, side: String, spec: ForgeConfigSpec, enableDirForConfig: Boolean = true) {
    val fixedSideString = side.lowercase()

    when(fixedSideString) {
        "common" -> {
            if (enableDirForConfig) spec.commonConfig(modId.configPath(side))
            else spec.commonConfig(modId.configFileName(side))
        }
        "client" -> {
            if (enableDirForConfig) spec.clientConfig(modId.configPath(side))
            else spec.clientConfig(modId.configFileName(side))
        }
        "server" -> {
            if (enableDirForConfig) spec.serverConfig(modId.configPath(side))
            else spec.serverConfig(modId.configFileName(side))
        }
        else -> CrashReport.forThrowable(
            UnsupportedOperationException(
                "Config creation operation is not successful: Cannot invoke method ${::makeConfig}. Reason: param 'side' is not valid! Provided: $side. Supported: 'common', 'client', 'server'"
            ),
            "Please, read an error."
        )
    }
}

fun String.configFileName(id: String) = "${this}_$id".configFile()

fun String.configPath(id: String): String = "$this/${id.configFile()}"

fun String.configFile(): String = "$this.toml"

fun ForgeConfigSpec.commonConfig(id: String) {
    context.commonConfig(this, id)
}

fun ForgeConfigSpec.clientConfig(id: String) {
    context.clientConfig(this, id)
}

fun ForgeConfigSpec.serverConfig(id: String) {
    context.serverConfig(this, id)
}

fun ModLoadingContext.commonConfig(spec: ForgeConfigSpec, id: String) {
    this.registerConfig(ModConfig.Type.COMMON, spec, id)
}

fun ModLoadingContext.clientConfig(spec: ForgeConfigSpec, id: String) {
    this.registerConfig(ModConfig.Type.CLIENT, spec, id)
}

fun ModLoadingContext.serverConfig(spec: ForgeConfigSpec, id: String) {
    this.registerConfig(ModConfig.Type.SERVER, spec, id)
}

fun intArray(size: Int) = net.minecraft.util.IntArray(size)
