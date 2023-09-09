package com.algorithmlx.inscribers.compact.crafttweaker

import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import com.algorithmlx.inscribers.reloc
import com.blamejared.crafttweaker.api.CraftTweakerAPI
import com.blamejared.crafttweaker.api.annotations.ZenRegister
import com.blamejared.crafttweaker.api.item.IIngredient
import com.blamejared.crafttweaker.api.managers.IRecipeManager
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import org.openzen.zencode.java.ZenCodeType

@IRecipeHandler.For(InscriberRecipe::class)
@ZenCodeType.Name("mods.inscribers.InscriberRecipe")
@ZenRegister
class InscriberCraftTweakerRecipe: IRecipeManager, IRecipeHandler<InscriberRecipe> {
    @ZenCodeType.Method
    fun addRecipe(id: String, energy: Int, time: Int, output: ItemStack, inputs: IIngredient) {
        val fixedId = fixRecipeName(id)
        val rl = reloc(fixedId)
        CraftTweakerAPI.apply(ActionAddRecipe(this, InscriberRecipe(rl, inputs.asVanillaIngredient(), output, time, energy)))
    }
    @ZenCodeType.Method
    fun addRecipe(id: String, output: ItemStack, inputs: IIngredient) {
        this.addRecipe(id, 400, 1000, output, inputs)
    }

    override fun getRecipeType(): IRecipeType<out IRecipe<*>> = InscribersRecipeTypes.inscriberRecipe

    override fun dumpToCommandString(manager: IRecipeManager, recipe: InscriberRecipe): String =
        "${manager.commandString}${recipe.getId()}${recipe.result}${recipe.ingredients}"
}