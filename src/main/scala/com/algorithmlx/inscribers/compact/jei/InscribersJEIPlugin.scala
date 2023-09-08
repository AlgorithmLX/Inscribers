package com.algorithmlx.inscribers.compact.jei

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.api.JUtils
import com.algorithmlx.inscribers.api.helper.DistHelper
import com.algorithmlx.inscribers.compact.jei.category.InscriberRecipeCategory
import com.algorithmlx.inscribers.init.registry.{InscribersRecipeTypes, Register}
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeCategoryRegistration, IRecipeRegistration}
import mezz.jei.api.{IModPlugin, JeiPlugin}
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

//noinspection ScalaUnusedSymbol
@JeiPlugin
class InscribersJEIPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = reloc("integration_jei")

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {
    val guiHelper = registration.getJeiHelpers.getGuiHelper
    registration.addRecipeCategories(new InscriberRecipeCategory(guiHelper))
  }

  override def registerRecipeCatalysts(registration: IRecipeCatalystRegistration): Unit = {
    registration.addRecipeCatalyst(new ItemStack(Register.INSCRIBER_BLOCK.get()), reloc("inscriber"))
  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
    if (DistHelper.isLogicalClient) {
      val level = Minecraft.getInstance().level
      if (level == null) return
      val recipeManager = level.getRecipeManager
      val recipes = JUtils.getRecipe(recipeManager)

      registration.addRecipes(recipes.values(), reloc("inscriber"))
    }
  }
}
