package com.algorithmlx.inscribers.api.helper

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTUtil

object StackHelper {
    @JvmStatic
    fun getStackWithSize(stack: ItemStack, size: Int, container: Boolean): ItemStack {
        var stackshadow = stack

        if (size <= 0) {
            return if (container && stackshadow.hasContainerItem()) stackshadow.containerItem
            else ItemStack.EMPTY
        }

        stackshadow = stack.copy()
        stackshadow.count = size

        return stackshadow
    }

    @JvmStatic
    fun grow(stack: ItemStack, amount: Int): ItemStack = this.getStackWithSize(stack, stack.count + amount, false)

    @JvmStatic
    fun shrink(stack: ItemStack, amount: Int, container: Boolean) =
        if (stack.isEmpty)
            ItemStack.EMPTY
        else
            this.getStackWithSize(stack, stack.count - amount, container)

    @JvmStatic
    fun areItemEquals(stack: ItemStack, itemStack: ItemStack) =
        !stack.isEmpty && !itemStack.isEmpty && stack.sameItem(itemStack)

    @JvmStatic
    fun areStackEquals(stack: ItemStack, itemStack: ItemStack) =
        areItemEquals(stack, itemStack) && ItemStack.tagMatches(stack, itemStack)

    @JvmStatic
    fun canCombine(stack: ItemStack, itemStack: ItemStack): Boolean {
        if (!stack.isEmpty && itemStack.isEmpty) return true
        return areStackEquals(stack, itemStack) && (stack.count + itemStack.count) <= stack.maxStackSize
    }

    @JvmStatic
    fun combine(stack: ItemStack, itemStack: ItemStack): ItemStack {
        if (stack.isEmpty) itemStack.copy()
        return grow(stack, itemStack.count)
    }

    @JvmStatic
    fun compareTags(stack: ItemStack, itemStack: ItemStack): Boolean {
        if (!stack.hasTag()) return true
        if (stack.hasTag() && !itemStack.hasTag()) return false

        val stackSet = TagHelper.getTagCompound(stack)?.allKeys
        val itemStackSet = TagHelper.getTagCompound(itemStack)?.allKeys

        if (stackSet != null && itemStackSet != null) {
            for (key in stackSet) {
                if (itemStackSet.contains(key)) {
                    if (!NBTUtil.compareNbt(TagHelper.getTag(stack, key), TagHelper.getTag(itemStack, key), true))
                        return false
                }
                else return false
            }
        }

        return true
    }
}
