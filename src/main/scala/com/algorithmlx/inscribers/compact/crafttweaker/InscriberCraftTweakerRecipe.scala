package com.algorithmlx.inscribers.compact.crafttweaker

import com.algorithmlx.inscribers.Constant
import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.init.InscribersRecipeTypes
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import com.blamejared.crafttweaker.api.CraftTweakerAPI
import com.blamejared.crafttweaker.api.annotations.ZenRegister
import com.blamejared.crafttweaker.api.item.IIngredient
import com.blamejared.crafttweaker.api.managers.IRecipeManager
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipe, IRecipeType}
import org.openzen.zencode.java.ZenCodeType

@IRecipeHandler.For(classOf[InscriberRecipe])
@ZenCodeType.Name(s"mods.${Constant.ModId}.Inscriber")
@ZenRegister
class InscriberCraftTweakerRecipe extends IRecipeManager with IRecipeHandler[InscriberRecipe] {
  @ZenCodeType.Method
  def addRecipe(id: String, energy: Int, time: Int, output: ItemStack, inputs: IIngredient): Unit = {
    val fixedId = fixRecipeName(id)
    val rl = reloc(fixedId)

    CraftTweakerAPI.apply(new ActionAddRecipe(this, new InscriberRecipe(
      rl, inputs.asVanillaIngredient(), output, time, energy
    )))
  }

  override def getRecipeType: IRecipeType[_ <: IRecipe[_]] = InscribersRecipeTypes.inscriberRecipe

  override def dumpToCommandString(manager: IRecipeManager, recipe: InscriberRecipe): String = manager.getCommandString + recipe.getId + recipe.result + recipe.getIngredients
}
