package com.algorithmlx.inscribers.recipe

import com.google.gson.{JsonElement, JsonObject}
import net.minecraft.inventory.IInventory
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.Ingredient.SingleItemList
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{JSONUtils, ResourceLocation}
import net.minecraftforge.registries.ForgeRegistries

import java.util.stream.StreamSupport

object IngredientHelper {
  def test[T <: IInventory](ingredient: Ingredient, inv: T): Boolean = {
    if (ingredient.getItems.length != inv.getContainerSize) return false
    for (i <- 0 until inv.getContainerSize) {
      val item = ingredient.getItems.apply(i)
      val input: ItemStack = inv.getItem(i)
      if (item.getItem != input.getItem || item.getCount > input.getCount) return false
    }

    true
  }

  def getStack(`object`: JsonObject): ItemStack = {
    var count = 1
    if (`object`.has("count")) count = JSONUtils.getAsInt(`object`, "count")
    val itemLoc = new ResourceLocation(JSONUtils.getAsString(`object`, "item"))
    val item: Item = ForgeRegistries.ITEMS.getValue(itemLoc)
    new ItemStack(item, count)
  }

  def fromJson(jsonElement: JsonElement): Ingredient = {
    if (jsonElement.isJsonArray) return Ingredient.fromValues(
      StreamSupport.stream(jsonElement.getAsJsonArray.spliterator(), false)
        .map { `object` =>
          val stack = getStack(`object`.getAsJsonObject)
          new SingleItemList(stack)
        }
    ) else if (jsonElement.isJsonObject) {
      val `object` = jsonElement.getAsJsonObject
      return Ingredient.fromValues(java.util.stream.Stream.of(new SingleItemList(getStack(`object`))))
    }

    null
  }
}
