package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.ModId
import com.algorithmlx.inscribers.block.Inscriber
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.block.Block
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

@Mod.EventBusSubscriber(value = [Dist.DEDICATED_SERVER, Dist.CLIENT], modid = ModId, bus = Mod.EventBusSubscriber.Bus.MOD)
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
    val inscriberBlock: RegistryObject<Inscriber> = block(INSCRIBER_ID, ::Inscriber) { BlockItem(it, Item.Properties().tab(tab)) }
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
    }

    fun <T : IForgeRegistryEntry<T>> deferred(reg: IForgeRegistry<T>): DeferredRegister<T> =
        DeferredRegister.create(reg, ModId)

    fun <T: Block> block(id: String, block: () -> T, item: (T) -> Item): RegistryObject<T> {
        val registeredBlock = this.block.register(id, block)
        this.item.register(id) { item.invoke(registeredBlock.get()) }
        return registeredBlock
    }
}
