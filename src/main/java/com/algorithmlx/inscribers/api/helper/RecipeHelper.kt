package com.algorithmlx.inscribers.api.helper

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.util.RecipeMatcher
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

interface RecipeHelper: IRecipe<IInventory> {
    override fun assemble(pInv: IInventory): ItemStack = this.result(InvWrapper(pInv))

    override fun matches(pInv: IInventory, pLevel: World): Boolean = this.matches(InvWrapper(pInv))

    override fun getRemainingItems(pInv: IInventory): NonNullList<ItemStack> = this.getRemainingItems(InvWrapper(pInv))

    fun result(handler: IItemHandler): ItemStack

    fun result(inv: IInventory): ItemStack = this.result(InvWrapper(inv))

    fun matches(handler: IItemHandler): Boolean = this.matches(handler, 0, handler.slots)

    fun matches(handler: IItemHandler, startIndex: Int, endIndex: Int): Boolean {
        val inputs = NonNullList.create<ItemStack>()

        for (i in startIndex ..< endIndex) {
            inputs.add(handler.getStackInSlot(i))
        }

        return RecipeMatcher.findMatches(inputs, this.ingredients) != null
    }

    fun getRemainingItems(handler: IItemHandler): NonNullList<ItemStack> {
        val items = NonNullList.withSize(handler.slots, ItemStack.EMPTY)

        for (i in 0 ..< items.size) {
            val stack = handler.getStackInSlot(i)
            if (stack.hasContainerItem()) items[i] = stack.containerItem
        }

        return items
    }
}