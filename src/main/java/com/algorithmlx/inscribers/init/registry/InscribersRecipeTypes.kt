package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.recipe.CastRecipeType
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.item.crafting.IRecipeType

object InscribersRecipeTypes {
    val matrixInscriberRecipe: IRecipeType<InscriberRecipe> = CastRecipeType.invoke<InscriberRecipe>("inscriber").get()
}