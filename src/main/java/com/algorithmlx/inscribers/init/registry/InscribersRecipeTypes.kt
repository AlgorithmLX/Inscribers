package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.recipe.CastRecipeType
import com.algorithmlx.inscribers.recipe.InscriberRecipe

object InscribersRecipeTypes {
    val inscriberRecipe: CastRecipeType<InscriberRecipe> = CastRecipeType.invoke("inscriber")
}