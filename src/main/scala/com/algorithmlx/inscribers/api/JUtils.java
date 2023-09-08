package com.algorithmlx.inscribers.api;

public class JUtils {
    public static net.minecraft.inventory.IInventory invHelper(net.minecraft.item.ItemStack[] stacks) {
        return new net.minecraft.inventory.Inventory(stacks);
    }

    public static java.util.Map<
            net.minecraft.util.ResourceLocation,
            net.minecraft.item.crafting.IRecipe<net.minecraft.inventory.IInventory>
            > getRecipe(net.minecraft.item.crafting.RecipeManager recipeManager) {
        return recipeManager.byType(com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes.inscriberRecipe());
    }
}
