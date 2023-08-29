package com.algorithmlx.inscribers.compact.jei.category

import com.algorithmlx.inscribers.Constant
import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.init.Register
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

class InscriberRecipeCategory(guiHelper: IGuiHelper) extends IRecipeCategory[InscriberRecipe] {
  private val texture: ResourceLocation = reloc("textures/gui/jei/inscriber.png")
  private val background: IDrawable = guiHelper.createDrawable(texture, 0, 0, null, null) // null replace to size
  private val icon: IDrawable = guiHelper.createDrawableIngredient(new ItemStack(Register.INSCRIBER_BLOCK.get()))

  override def getUid: ResourceLocation = reloc("inscriber")

  override def getRecipeClass: Class[_ <: InscriberRecipe] = classOf[InscriberRecipe]

  //noinspection ScalaDeprecation
  override def getTitle: String = new TranslationTextComponent(s"jei.${Constant.ModId}.category.inscriber").toString

  override def getBackground: IDrawable = this.background

  override def getIcon: IDrawable = this.icon

  override def setIngredients(recipe: InscriberRecipe, ingredients: IIngredients): Unit = {
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem)
    ingredients.setInputIngredients(recipe.getIngredients)
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: InscriberRecipe, ingredients: IIngredients): Unit = {
    val stackGroup = recipeLayout.getItemStacks
    val inputs = ingredients.getInputs(VanillaTypes.ITEM)
    val outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0)

    stackGroup.init(0, false, 46, 146)
    stackGroup.set(0, outputs)
  }
}
