package com.algorithmlx.inscribers.api;

import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen;
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes;
import com.algorithmlx.inscribers.menu.InscriberContainerMenu;
import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class JUtils {
    public static IInventory invHelper(ItemStack[] stacks) {
        return new Inventory(stacks);
    }

    public static Map<ResourceLocation, IRecipe<IInventory>> getRecipe(RecipeManager recipeManager) {
        return recipeManager.byType(InscribersRecipeTypes.inscriberRecipe());
    }

    public static ContainerBuilder.ForgeContainerFactory<InscriberContainerMenu> containerFactory() {
        return InscriberContainerMenu::new;
    }

    public static NonNullSupplier<ContainerBuilder.ScreenFactory<InscriberContainerMenu, InscriberMenuScreen>> screenFactory() {
        return ()-> InscriberMenuScreen::new;
    }
}
