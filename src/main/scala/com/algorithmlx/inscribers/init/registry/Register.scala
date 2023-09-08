package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.Constant
import com.algorithmlx.inscribers.api.JUtils
import com.algorithmlx.inscribers.block._
import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen
import com.algorithmlx.inscribers.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.recipe.InscriberSerializer
import com.tterrag.registrate.Registrate
import com.tterrag.registrate.builders.ContainerBuilder
import com.tterrag.registrate.builders.ContainerBuilder.{ForgeContainerFactory, ScreenFactory}
import com.tterrag.registrate.util.entry._
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.{BlockItem, ItemGroup, ItemStack}
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.ToolType
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries}

object Register {
  private val TAB: ItemGroup = new ItemGroup(Constant.ModId) {
    override def makeIcon(): ItemStack = INSCRIBER_ITEM.asStack()
  }
  private val REG = Registrate.create(Constant.ModId).itemGroup(()=> TAB)
  private val RECIPES: DeferredRegister[IRecipeSerializer[_]] = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constant.ModId)

  val INSCRIBER_RECIPE: RegistryObject[InscriberSerializer] = RECIPES.register("inscriber", () => new InscriberSerializer)
  val INSCRIBER_BLOCK: BlockEntry[Inscriber] = REG.`object`("inscriber")
    .block(p => new Inscriber(p))
    .properties(p => p.noOcclusion().harvestLevel(3).harvestTool(ToolType.PICKAXE))
    .item()
      .defaultModel()
      .build()
    .tileEntity(a => new InscriberBlockEntity(a))
      .build()
    .register()
  val INSCRIBER_ITEM: ItemEntry[BlockItem] = ItemEntry.cast(INSCRIBER_BLOCK.getSibling(ForgeRegistries.ITEMS))
  val INSCRIBER_BLOCK_ENTITY: TileEntityEntry[TileEntity] = TileEntityEntry.cast(INSCRIBER_BLOCK.getSibling(ForgeRegistries.TILE_ENTITIES))
  val INSCRIBER_MENU_TYPE: ContainerEntry[InscriberContainerMenu] = REG.`object`("inscriber")
    .container(JUtils.containerFactory(), JUtils.screenFactory()).register()

  def init(): Unit = {
    val bus = FMLJavaModLoadingContext.get().getModEventBus
    RECIPES.register(bus)
  }
}
