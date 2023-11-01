package com.algorithmlx.inscribers.compact.jei

import com.algorithmlx.inscribers.api.isLogicalClient
import com.algorithmlx.inscribers.api.mc
import com.algorithmlx.inscribers.compact.jei.category.InscriberRecipeCategory
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.init.registry.Register
import com.algorithmlx.inscribers.reloc
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

@JeiPlugin
class InscribersJEIPlugin: IModPlugin {
    override fun getPluginUid(): ResourceLocation = reloc("jei")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper = registration.jeiHelpers.guiHelper
        registration.addRecipeCategories(InscriberRecipeCategory(guiHelper))
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(ItemStack(Register.basicInscriberBlock.get()), reloc("inscriber"))
        registration.addRecipeCatalyst(ItemStack(Register.advancedInscriber.get()), reloc("inscriber"))
        registration.addRecipeCatalyst(ItemStack(Register.improvedInscriber.get()), reloc("inscriber"))
        registration.addRecipeCatalyst(ItemStack(Register.eliteInscriber.get()), reloc("inscriber"))
        registration.addRecipeCatalyst(ItemStack(Register.perfectInscriber.get()), reloc("inscriber"))
        registration.addRecipeCatalyst(ItemStack(Register.maximizedInscriber.get()), reloc("inscriber"))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        if (isLogicalClient()) {
            val level = mc.level ?: return
            val recipeManager = level.recipeManager
            val recipe = recipeManager.byType(InscribersRecipeTypes.matrixInscriberRecipe)

            registration.addRecipes(recipe.values, reloc("inscriber"))
        }
    }
}