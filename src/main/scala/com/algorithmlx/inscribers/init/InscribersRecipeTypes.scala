package com.algorithmlx.inscribers.init

import com.algorithmlx.inscribers.recipe.{CastRecipeType, InscriberRecipe}
import net.minecraft.item.crafting.IRecipeType

object InscribersRecipeTypes {
  val inscriberRecipe = CastRecipeType.invoke[InscriberRecipe]("inscriber")
}
