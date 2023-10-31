package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.api.helper.RecipeHelper
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.init.registry.Register
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistryEntry
import kotlin.math.sqrt

class MatrixInscriberRecipe(
        val id: ResourceLocation,
        val ingredient: NonNullList<Ingredient>,
        val result: ItemStack,
        val width: Int,
        val height: Int
): RecipeHelper {
    override fun result(handler: IItemHandler): ItemStack = this.result.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean =
            pWidth >= this.width && pHeight >= this.height

    override fun matches(handler: IItemHandler): Boolean {
        val size = sqrt(handler.slots.toDouble()).toInt()
        for (i in 0 .. size - this.width) {
            for (j in 0 .. size - this.height) {
                if (this.matching(handler, i, j,true)) return true
                if (this.matching(handler, i, j, false)) return true
            }
        }

        return false
    }

    override fun getIngredients(): NonNullList<Ingredient> = this.ingredient

    override fun getResultItem(): ItemStack = this.result

    override fun getId(): ResourceLocation = this.id

    override fun getSerializer(): IRecipeSerializer<*> = Register.inscriberMatrixRecipe.get()

    override fun getType(): IRecipeType<*> = InscribersRecipeTypes.matrixInscriberRecipe

    private fun matching(handler: IItemHandler, x: Int, y: Int, mirror: Boolean): Boolean {
        val size = sqrt(handler.slots.toDouble()).toInt()
        for (i in 0 until size) {
            for (j in 0 until size) {
                val k = i - x
                val l = j - y
                var ingredient = Ingredient.EMPTY
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    ingredient = if (mirror) this.ingredient[this.width - k - 1 + l * this.width]
                    else this.ingredient[k + l * this.width]
                    if (!ingredient.test(handler.getStackInSlot(i + j * this.width)))
                        return false
                }
            }
        }

        return true
    }

    companion object {
        @JvmStatic
        fun patternFromJson(jsonArray: JsonArray): Array<String?> {
            val asString = arrayOfNulls<String>(jsonArray.size())
            for (i in asString.indices) {
                val string = JSONUtils.convertToString(jsonArray.get(i), "pattern[$i]")
                if (i > 0 && asString[0]?.length != string.length) {
                    throw JsonSyntaxException("Invalid pattern: each row must be the same width")
                }

                asString[i] = string
            }

            return asString
        }
    }

    class Serializer: IRecipeSerializer<MatrixInscriberRecipe>, ForgeRegistryEntry<IRecipeSerializer<*>>() {
        override fun fromJson(pRecipeId: ResourceLocation, pJson: JsonObject): MatrixInscriberRecipe {
            val map = ShapedRecipe.keyFromJson(JSONUtils.getAsJsonObject(pJson, "where"))
            val pattern = ShapedRecipe.shrink(*patternFromJson(JSONUtils.getAsJsonArray(pJson, "pattern")))
            val width = pattern[0].length
            val height = pattern.size

            if (width > 6 || height > 6)
                throw JsonSyntaxException("Pattern size is large then 36 slots...")

            val ingredient = ShapedRecipe.dissolvePattern(pattern, map, width, height)
            val output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(pJson, "result"))

            return MatrixInscriberRecipe(pRecipeId, ingredient, output, width, height)
        }

        override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: PacketBuffer): MatrixInscriberRecipe? {
            TODO("Not yet implemented")
        }

        override fun toNetwork(pBuffer: PacketBuffer, pRecipe: MatrixInscriberRecipe) {
            TODO("Not yet implemented")
        }

    }
}