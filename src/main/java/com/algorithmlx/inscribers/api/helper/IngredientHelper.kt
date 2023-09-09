package com.algorithmlx.inscribers.api.helper

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import java.util.stream.Stream
import java.util.stream.StreamSupport

object IngredientHelper {
    @JvmStatic
    fun <T: IInventory> test(ingredient: Ingredient, inventory: T): Boolean {
        if (ingredient.items.size != inventory.containerSize) return false
        for (i in 0 until inventory.containerSize) {
            val ingredientItem = ingredient.items[i]
            val inputItem: ItemStack = inventory.getItem(i)
            if (ingredientItem.item != inputItem.item || ingredientItem.count > inputItem.count) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun getStack(`object`: JsonObject): ItemStack {
        var count = 1
        if (`object`.has("count")) {
            count = JSONUtils.getAsInt(`object`, "count")
        }
        val itemLocation = ResourceLocation(JSONUtils.getAsString(`object`, "item"))
        val item: Item? = ForgeRegistries.ITEMS.getValue(itemLocation)
        return ItemStack(item!!, count)
    }

    @JvmStatic
    fun fromJson(jsonElement: JsonElement): Ingredient? {
        if (jsonElement.isJsonArray) {
            return Ingredient.fromValues(
                StreamSupport.stream(jsonElement.asJsonArray.spliterator(), false)
                    .map { `object` ->
                        val stack = getStack(`object`.asJsonObject)
                        Ingredient.SingleItemList(stack)
                    }
            )
        } else if (jsonElement.isJsonObject) {
            val `object`: JsonObject = jsonElement.asJsonObject
            return Ingredient.fromValues(Stream.of(Ingredient.SingleItemList(getStack(`object`))))
        }
        return null
    }
}