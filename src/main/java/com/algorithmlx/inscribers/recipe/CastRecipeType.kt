package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.reloc
import net.minecraft.inventory.IInventory
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType

data class CastRecipeType<T: IRecipe<*>>(val id: String): IRecipeType<T> {
    override fun toString(): String = reloc(this.id).toString()

    companion object {
        @JvmStatic
        fun <X: IRecipe<*>> invoke(id: String): CastRecipeType<X> = CastRecipeType(id)
    }
}