package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.reloc
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation

data class CastRecipeType<T: IRecipe<*>>(val id: ResourceLocation) {
    fun getString(): String = id.toString()

    fun get(): IRecipeType<T> = object : IRecipeType<T> {
        override fun toString(): String = getString()
    }

    companion object {
        @JvmStatic
        operator fun <X: IRecipe<*>> invoke(id: String): CastRecipeType<X> = CastRecipeType(reloc(id))
        @JvmStatic
        operator fun <X: IRecipe<*>> invoke(id: ResourceLocation): CastRecipeType<X> = CastRecipeType(id)
        @JvmStatic
        fun <X: IRecipe<*>> String.recipeType(): CastRecipeType<X> = invoke(this)
        @JvmStatic
        fun <X: IRecipe<*>> ResourceLocation.recipeType(): CastRecipeType<X> = invoke(this)
        @JvmStatic
        operator fun <X: IRecipe<*>> invoke(modId: String, id: String): CastRecipeType<X> = CastRecipeType(ResourceLocation(modId, id))
        @JvmStatic
        fun <X: IRecipe<*>> String.recipeType(id: String): CastRecipeType<X> = CastRecipeType(this, id)
    }
}