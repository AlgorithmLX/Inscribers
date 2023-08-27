package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.api.helper.RecipeHelper
import com.algorithmlx.inscribers.init.{InscribersRecipeTypes, Register}
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer, IRecipeType, Ingredient}
import net.minecraft.util.{NonNullList, ResourceLocation}
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandler

class InscriberRecipe(
  recipeType: IRecipeType[InscriberRecipe],
  id: ResourceLocation,
  ingredient: Ingredient,
  result: ItemStack,
  time: Int,
  energyPerTick: Int
) extends RecipeHelper {
  def this(id: ResourceLocation, ingredient: Ingredient, result: ItemStack, time: Int, energyPerTick: Int) {
    this(InscribersRecipeTypes.inscriberRecipe, id, ingredient, result, time, energyPerTick)
  }

  def getIngredient: Ingredient = {
    this.ingredient
  }

  def getTime: Int = {
    this.time
  }

  def getEnergyCount: Int = {
    this.energyPerTick
  }

  override def result(itemHandler: IItemHandler): ItemStack = this.getResultItem

  override def canCraftInDimensions(p_194133_1_ : Int, p_194133_2_ : Int): Boolean = true

  override def getResultItem: ItemStack = this.result.copy()

  override def getId: ResourceLocation = this.id

  override def getSerializer: IRecipeSerializer[_] = Register.INSCRIBER_RECIPE.get()

  override def getType: IRecipeType[_] = this.recipeType
}
