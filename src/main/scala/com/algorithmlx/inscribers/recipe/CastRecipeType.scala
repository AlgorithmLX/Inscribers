package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.Constant
import net.minecraft.inventory.IInventory
import net.minecraft.item.crafting.{IRecipe, IRecipeType}
import net.minecraft.util.ResourceLocation

object CastRecipeType {
  def invoke[X <: IRecipe[IInventory]](id: String): Invoker[X] = new Invoker[X](id)

  private class Invoker[T <: IRecipe[_]](recipeId: String) extends IRecipeType[T] {
    override def toString: String = new ResourceLocation(Constant.ModId, recipeId).toString
  }
}
