package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.recipe.{CastRecipeType, InscriberRecipe}

object InscribersRecipeTypes {
  val inscriberRecipe: CastRecipeType[InscriberRecipe] = CastRecipeType.invoke[InscriberRecipe]("inscriber")
}
