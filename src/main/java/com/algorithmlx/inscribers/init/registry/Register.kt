package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.ModId
import com.algorithmlx.inscribers.api.isPhysicalClient
import com.algorithmlx.inscribers.block.Inscriber
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.client.gui.ScreenManager
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.*
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

@Suppress("MemberVisibilityCanBePrivate", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object Register {
    private val tab = InscriberTab.create(ModId)
    private const val INSCRIBER_ID = "inscriber"

    val recipes = deferred(ForgeRegistries.RECIPE_SERIALIZERS)
    val block = deferred(ForgeRegistries.BLOCKS)
    val item = deferred(ForgeRegistries.ITEMS)
    val blockEntity = deferred(ForgeRegistries.TILE_ENTITIES)
    val menuType = deferred(ForgeRegistries.CONTAINERS)

    val inscriberRecipe: RegistryObject<InscriberRecipe.Serializer> = recipes.register(INSCRIBER_ID, InscriberRecipe::Serializer)
    val inscriberBlock: RegistryObject<Inscriber> = block.register(INSCRIBER_ID, ::Inscriber)
    val inscriberBlockEntity: RegistryObject<TileEntityType<InscriberBlockEntity>> = blockEntity.register(INSCRIBER_ID) {
        TileEntityType.Builder.of(::InscriberBlockEntity, inscriberBlock.get()).build(null)
    }
    val inscriberContainerMenu: RegistryObject<ContainerType<InscriberContainerMenu>> = menuType.register(INSCRIBER_ID) {
        IForgeContainerType.create(::InscriberContainerMenu)
    }

    @JvmStatic
    fun init() {
        val bus = FMLJavaModLoadingContext.get().modEventBus
        recipes.register(bus)
        block.register(bus)
        item.register(bus)
        blockEntity.register(bus)
        menuType.register(bus)

        bus.addListener(::registerBlockItems)

        if (isPhysicalClient()) {
            ScreenManager.register(this.inscriberContainerMenu.get(), ::InscriberMenuScreen)
        }
    }

    fun <T : IForgeRegistryEntry<T>> deferred(reg: IForgeRegistry<T>): DeferredRegister<T> =
        DeferredRegister.create(reg, ModId)

    private fun registerBlockItems(evt: RegistryEvent.Register<Item>) {
        block.entries.stream().map { it.get() }.forEach {
            evt.registry.register(BlockItem(it, Item.Properties().tab(tab)))
        }
    }
}
