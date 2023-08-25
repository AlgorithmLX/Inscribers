package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.init.Register
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer, IRecipeType, Ingredient}
import net.minecraft.util.{NonNullList, ResourceLocation}
import net.minecraft.world.World

class InscriberRecipe(
  recipeType: IRecipeType[InscriberRecipe],
  id: ResourceLocation,
  ingredient: Ingredient,
  result: ItemStack,
  time: Int,
  energyPerTick: Int
) extends IRecipe[IInventory] {
  def this(id: ResourceLocation, ingredient: Ingredient, result: ItemStack, time: Int, energyPerTick: Int) {
    this(CastRecipeType.invoke("inscriber"), id, ingredient, result, time, energyPerTick)
  }

  override def matches(container : IInventory, level : World): Boolean = IngredientHelper.test(this.ingredient, container)

  override def assemble(container : IInventory): ItemStack = this.result.copy()

  override def canCraftInDimensions(p_194133_1_ : Int, p_194133_2_ : Int): Boolean = true

  override def getResultItem: ItemStack = this.result.copy()

  override def getIngredients: NonNullList[Ingredient] = {
    val nonNullList: NonNullList[Ingredient] = NonNullList.create()
    nonNullList.add(this.ingredient)
    nonNullList
  }

  override def getId: ResourceLocation = this.id

  override def getSerializer: IRecipeSerializer[_] = Register.INSCRIBER_RECIPE.get()

  override def getType: IRecipeType[_] = recipeType

  def getIngredient: Ingredient = {
    this.ingredient
  }

  def getTime: Int = {
    this.time
  }

  def getEnergyCount: Int = {
    this.energyPerTick
  }
}
