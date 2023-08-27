package com.algorithmlx.inscribers.compact.jei

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.api.DistHelper
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeCategoryRegistration, IRecipeRegistration}
import mezz.jei.api.{IModPlugin, JeiPlugin}
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation

@JeiPlugin
class InscribersJEIPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = reloc("integration_jei")

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {

  }

  override def registerRecipeCatalysts(registration: IRecipeCatalystRegistration): Unit = {

  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
    if (DistHelper.isLogicalClient) {
      val level = Minecraft.getInstance().level

      if (level == null) return

      val recipeManager = level.getRecipeManager
    }
  }
}
