package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.reloc
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation

data class CastRecipeType(val id: ResourceLocation) {
    fun getString(): String = id.toString()

    fun <T: IRecipe<*>> get(): IRecipeType<T> = object : IRecipeType<T> {
        override fun toString(): String = getString()
    }

    companion object {
        @JvmStatic
        operator fun invoke(id: String): CastRecipeType = CastRecipeType(reloc(id))
        @JvmStatic
        operator fun invoke(id: ResourceLocation): CastRecipeType = CastRecipeType(id)
        @JvmStatic
        fun String.recipeType(): CastRecipeType = invoke(this)
        @JvmStatic
        fun ResourceLocation.recipeType(): CastRecipeType = invoke(this)
        @JvmStatic
        operator fun invoke(modId: String, id: String): CastRecipeType = CastRecipeType(ResourceLocation(modId, id))
        @JvmStatic
        fun String.recipeType(id: String): CastRecipeType = CastRecipeType(this, id)
    }
}