package com.algorithmlx.inscribers.api.handler;

class JStackHelper {
    public static net.minecraft.inventory.IInventory invHelper(net.minecraft.item.ItemStack[] stacks) {
        return new net.minecraft.inventory.Inventory(stacks);
    }
}
