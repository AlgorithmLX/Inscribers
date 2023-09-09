package com.algorithmlx.inscribers.recipe

import com.algorithmlx.inscribers.api.helper.RecipeHelper
import com.algorithmlx.inscribers.api.helper.IngredientHelper
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.init.registry.Register
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry

class InscriberRecipe(
    @get:JvmName("recipeType")
    val recipeType: IRecipeType<InscriberRecipe>,
    @get:JvmName("id")
    val id: ResourceLocation,
    @get:JvmName("ingredient")
    val ingredient: Ingredient,
    @get:JvmName("result")
    val result: ItemStack,
    @get:JvmName("time")
    val time: Int,
    @get:JvmName("getEnergyCount")
    val energyPerTick: Int
): RecipeHelper {
    constructor(id: ResourceLocation, ingredient: Ingredient, result: ItemStack, time: Int, energyPerTick: Int): this(InscribersRecipeTypes.inscriberRecipe, id, ingredient, result, time, energyPerTick)

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = this.result.copy()

    override fun getId(): ResourceLocation = this.id

    override fun getSerializer(): IRecipeSerializer<*> = Register.inscriberRecipe.get()

    override fun getType(): IRecipeType<*> = this.recipeType

    override fun result(handler: IItemHandler): ItemStack = this.resultItem

    class Serializer: IRecipeSerializer<InscriberRecipe>, ForgeRegistryEntry<IRecipeSerializer<*>>() {
        override fun fromJson(pRecipeId: ResourceLocation, pJson: JsonObject): InscriberRecipe {
            val jsonIngredient = if (JSONUtils.isArrayNode(pJson, "ingredients")) JSONUtils.getAsJsonArray(
                pJson,
                "ingredients"
            ) else JSONUtils.getAsJsonObject(pJson, "ingredients")

            if (!pJson.has("result")) throw JsonSyntaxException("$pRecipeId is not valid! Missing argument: \"result\".")
            val itemStack: ItemStack? = if (pJson.get("result").isJsonObject) ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(pJson, "result"))
            else ForgeRegistries.ITEMS.getValue(ResourceLocation(JSONUtils.getAsString(pJson, "result")))
                ?.let { ItemStack(it) }

            val ingredient = IngredientHelper.fromJson(jsonIngredient)

            val time: Int = JSONUtils.getAsInt(pJson, "time", 400)
            val energy: Int = JSONUtils.getAsInt(pJson, "energy", 1000)

            return this.getSerial().invoke(pRecipeId, ingredient!!, itemStack!!, time, energy)!!
        }

        override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: PacketBuffer): InscriberRecipe? {
            val ingredient = Ingredient.fromNetwork(pBuffer)
            val itemStack = pBuffer.readItem()
            val time = pBuffer.readInt()
            val energy = pBuffer.readInt()
            return this.getSerial().invoke(pRecipeId, ingredient, itemStack, time, energy)
        }

        override fun toNetwork(pBuffer: PacketBuffer, pRecipe: InscriberRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer)
            pBuffer.writeItem(pRecipe.resultItem)
            pBuffer.writeInt(pRecipe.time)
            pBuffer.writeInt(pRecipe.energyPerTick)
        }

        private fun getSerial(): (ResourceLocation, Ingredient, ItemStack, Int, Int) -> InscriberRecipe? = ::InscriberRecipe
    }
}