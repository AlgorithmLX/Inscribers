package com.algorithmlx.inscribers.recipe

import com.google.gson.{JsonObject, JsonSyntaxException}
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipeSerializer, Ingredient, ShapedRecipe}
import net.minecraft.network.PacketBuffer
import net.minecraft.util.{JSONUtils, ResourceLocation}
import net.minecraftforge.registries.{ForgeRegistries, ForgeRegistryEntry}

class InscriberSerializer extends ForgeRegistryEntry[IRecipeSerializer[_]] with IRecipeSerializer[InscriberRecipe] {
  override def fromJson(p_199425_1_ : ResourceLocation, jsonObject : JsonObject): InscriberRecipe = {
    val jsonIngredient = if (JSONUtils.isArrayNode(jsonObject, "ingredients")) JSONUtils.getAsJsonArray(
      jsonObject,
      "ingredients"
    ) else JSONUtils.getAsJsonObject(jsonObject, "ingredients")

    if (!jsonObject.has("result")) throw new JsonSyntaxException(s"$p_199425_1_ is not valid! Missing argument: \"result\".")
    val itemStack: ItemStack = if (jsonObject.get("result").isJsonObject) ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(jsonObject, "result"))
    else new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(jsonObject, "result"))))

    val ingredient = IngredientHelper.fromJson(jsonIngredient)

    val time: Int = JSONUtils.getAsInt(jsonObject, "time", 400)
    val energy: Int = JSONUtils.getAsInt(jsonObject, "energy", 1000)

    this.getSerial.invoke(p_199425_1_, ingredient, itemStack, time, energy)
  }

  def getSerial:Serializer = (reloc, ingredient, result, time, energy) => new InscriberRecipe(reloc, ingredient, result, time, energy)

  override def fromNetwork(resourceLocation : ResourceLocation, byteBuf : PacketBuffer): InscriberRecipe = {
    val ingredient = Ingredient.fromNetwork(byteBuf)
    val itemStack = byteBuf.readItem()
    val time = byteBuf.readInt()
    val energy = byteBuf.readInt()
    this.getSerial.invoke(resourceLocation, ingredient, itemStack, time, energy)
  }

  override def toNetwork(byteBuf : PacketBuffer, value : InscriberRecipe): Unit = {
    value.getIngredient().toNetwork(byteBuf)
    byteBuf.writeItem(value.getResultItem)
    byteBuf.writeInt(value.getTime())
    byteBuf.writeInt(value.getEnergyCount())
  }

  @FunctionalInterface
  private trait Serializer {
    def invoke(id: ResourceLocation, ingredient: Ingredient, result: ItemStack, time: Int, energy: Int): InscriberRecipe
  }
}
