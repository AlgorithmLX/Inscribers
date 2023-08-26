package com.algorithmlx.inscribers.init

import com.algorithmlx.inscribers.Constant
import com.algorithmlx.inscribers.block._
import com.algorithmlx.inscribers.recipe.InscriberSerializer
import com.tterrag.registrate.Registrate
import com.tterrag.registrate.util.entry._
import net.minecraft.item.{BlockItem, ItemGroup, ItemStack}
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.tileentity.TileEntity
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

  def init(): Unit = {
    val bus = FMLJavaModLoadingContext.get().getModEventBus
    RECIPES.register(bus)
  }
}
