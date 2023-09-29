package com.algorithmlx.inscribers.container

import com.algorithmlx.inscribers.api.handler.StackHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.container.Container
import net.minecraft.item.ItemStack

class InscriberCraftingContainer(
    private val containerMenu: Container,
    private val handler: StackHandler,
    size: Int
): CraftingInventory(containerMenu, size, size) {
    override fun getContainerSize(): Int = this.handler.slots

    override fun isEmpty(): Boolean {
        for (i in 0 until this.containerSize) {
            if (!this.handler.getStackInSlot(i).isEmpty) return false
        }

        return true
    }

    override fun getItem(pIndex: Int): ItemStack = this.handler.getStackInSlot(pIndex)

    override fun removeItem(pIndex: Int, pCount: Int): ItemStack {
        val stack =  this.handler.extract(pIndex, pCount, false)

        this.containerMenu.slotsChanged(this)

        return stack
    }

    override fun removeItemNoUpdate(pIndex: Int): ItemStack {
        val stack = this.handler.getStackInSlot(pIndex)

        this.handler.setStackInSlot(pIndex, ItemStack.EMPTY)

        return stack
    }

    override fun setItem(pIndex: Int, pStack: ItemStack) {
        this.handler.setStackInSlot(pIndex, pStack)
        this.containerMenu.slotsChanged(this)
    }

    override fun setChanged() {}

    override fun stillValid(pPlayer: PlayerEntity): Boolean = true

    override fun clearContent() {
        for (i in 0 until this.containerSize) this.handler.setStackInSlot(i, ItemStack.EMPTY)
    }
}