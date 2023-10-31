package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.recipe.CastRecipeType
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import com.algorithmlx.inscribers.recipe.MatrixInscriberRecipe
import net.minecraft.item.crafting.IRecipeType

object InscribersRecipeTypes {
    val inscriberRecipe: IRecipeType<InscriberRecipe> = CastRecipeType.invoke<InscriberRecipe>("inscriber").get()
    val matrixInscriberRecipe: IRecipeType<MatrixInscriberRecipe> = CastRecipeType.invoke<MatrixInscriberRecipe>("matrix").get()
}