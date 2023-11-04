package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.LOGGER
import com.algorithmlx.inscribers.ModId
import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.block.StandaloneInscriber
import com.algorithmlx.inscribers.block.entity.StandaloneInscriberBlockEntity
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.block.Block
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

@Suppress("MemberVisibilityCanBePrivate", "Nullability_mismatch_based_on_java_annotations")
object Register {
    private val basicInscriberExt = object : StandaloneInscriber(defaultProperties()) {
        override fun getTier(): IInscriber.InscriberTier = IInscriber.InscriberTier.BASIC
    }
    private val improvedInscriberExt = object : StandaloneInscriber(defaultProperties()) {
        override fun getTier(): IInscriber.InscriberTier = IInscriber.InscriberTier.IMPROVED
    }
    private val advancedInscriberExt = object : StandaloneInscriber(defaultProperties()) {
        override fun getTier(): IInscriber.InscriberTier = IInscriber.InscriberTier.ADVANCED
    }
    private val eliteInscriberExt = object : StandaloneInscriber(defaultProperties()) {
        override fun getTier(): IInscriber.InscriberTier = IInscriber.InscriberTier.ELITE
    }
    private val perfectInscriberExt = object : StandaloneInscriber(defaultProperties()) {
        override fun getTier(): IInscriber.InscriberTier = IInscriber.InscriberTier.PERFECT
    }
    private val maximizedInscriberExt = object : StandaloneInscriber(defaultProperties()) {
        override fun getTier(): IInscriber.InscriberTier = IInscriber.InscriberTier.MAXIMIZED
    }

    @JvmField
    val tab = InscriberTab.create(ModId)
    private const val INSCRIBER_ID = "inscriber"

    val recipes = deferred(ForgeRegistries.RECIPE_SERIALIZERS)
    val block = deferred(ForgeRegistries.BLOCKS)
    val item = deferred(ForgeRegistries.ITEMS)
    val blockEntity = deferred(ForgeRegistries.TILE_ENTITIES)
    val menuType = deferred(ForgeRegistries.CONTAINERS)

    val inscriberRecipe: RegistryObject<InscriberRecipe.Serializer> = recipes.register("inscriber", InscriberRecipe::Serializer)
    val basicInscriberBlock: RegistryObject<StandaloneInscriber> = block("basic".pluz(INSCRIBER_ID), this::basicInscriberExt)
    val improvedInscriber: RegistryObject<StandaloneInscriber> = block("improved".pluz(INSCRIBER_ID), this::improvedInscriberExt)
    val advancedInscriber: RegistryObject<StandaloneInscriber> = block("advanced".pluz(INSCRIBER_ID), this::advancedInscriberExt)
    val eliteInscriber: RegistryObject<StandaloneInscriber> = block("elite".pluz(INSCRIBER_ID), this::eliteInscriberExt)
    val perfectInscriber: RegistryObject<StandaloneInscriber> = block("perfect".pluz(INSCRIBER_ID), this::perfectInscriberExt)
    val maximizedInscriber: RegistryObject<StandaloneInscriber> = block("maximized".pluz(INSCRIBER_ID), this::maximizedInscriberExt)
    val inscriberBlockEntity: RegistryObject<TileEntityType<StandaloneInscriberBlockEntity>>
    init {
        val blockArray: MutableList<Block> = mutableListOf()

        ForgeRegistries.BLOCKS.values.stream().map {
            if (it is IInscriber) {
                IInscriber.boundedInscribers.add(it)
                LOGGER.debug("Added {} as supported block entity", it)
                blockArray.add(it)
            }
        }

        inscriberBlockEntity = blockEntity.register(INSCRIBER_ID) {
            TileEntityType.Builder.of(
                ::StandaloneInscriberBlockEntity,
                *blockArray.toTypedArray()
            ).build(null)
        }
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

    fun <T: Block> block(id: String, block: () -> T): RegistryObject<T> = block(id, block) { BlockItem(it, Item.Properties().tab(tab)) }

    fun <T: Block> block(id: String, block: () -> T, item: (T) -> Item): RegistryObject<T> {
        val registeredBlock = this.block.register(id, block)
        this.item.register(id) { item.invoke(registeredBlock.get()) }
        return registeredBlock
    }

    fun String.pluz(id: String): String = "${this}_$id"
}
