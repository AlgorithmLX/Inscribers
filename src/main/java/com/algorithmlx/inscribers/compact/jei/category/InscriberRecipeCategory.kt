package com.algorithmlx.inscribers.compact.jei.category

import com.algorithmlx.inscribers.api.basedText
import com.algorithmlx.inscribers.init.registry.Register
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import com.algorithmlx.inscribers.reloc
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class InscriberRecipeCategory(guiHelper: IGuiHelper): IRecipeCategory<InscriberRecipe> {
    private val texture = reloc("textures/gui/jei/inscriber.png")
    private val bg: IDrawable
    private val icon: IDrawable

    init {
        this.bg = guiHelper.createDrawable(texture, 0, 0, 130, 168)
        this.icon = guiHelper.createDrawableIngredient(ItemStack(Register.basicInscriberBlock.get()))
    }

    override fun getUid(): ResourceLocation = reloc("inscriber")

    override fun getRecipeClass(): Class<out InscriberRecipe> = InscriberRecipe::class.java

    override fun getTitle(): String = basedText("jei", "category.inscriber")

    override fun getBackground(): IDrawable = this.bg

    override fun getIcon(): IDrawable = this.icon

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: InscriberRecipe, ingredients: IIngredients) {
        val group = recipeLayout.itemStacks
        val inputs = ingredients.getInputs(VanillaTypes.ITEM)
        val outputs = ingredients.getOutputs(VanillaTypes.ITEM)[0]

        group.init(0, false, 46, 146)
        group.set(0, outputs)

        for (i in 1 until 36) {
            for (j in 0 until  6) {
                for (k in 0 until 6) {
                    group.init(i, true, j * 18, k * 18)
                }
            }
            group.set(i, inputs[i])
        }
    }

    override fun setIngredients(recipe: InscriberRecipe, ingredients: IIngredients) {
        ingredients.setOutput(VanillaTypes.ITEM, recipe.result)
        ingredients.setInputIngredients(recipe.ingredients)
    }
}