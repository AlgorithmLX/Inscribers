package com.algorithmlx.inscribers.api.handler

import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandlerModifiable


class SidedItemHandlerModifiable(
    private val stackHandler: StackHandler,
    private val direction: Direction,
    private val canInsert: ((Int, ItemStack, Direction) -> Boolean)?,
    private val canExtract: ((Int, Direction) -> Boolean)?
): IItemHandlerModifiable {
    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        this.stackHandler.setStackInSlot(slot, stack)
    }

    override fun getSlots(): Int = this.stackHandler.slots

    override fun getStackInSlot(slot: Int): ItemStack = this.stackHandler.getStackInSlot(slot)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY

        if (this.isItemValid(slot, stack)) return stack

        return this.stackHandler.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (this.canExtract != null && !this.canExtract.invoke(slot, this.direction)) return ItemStack.EMPTY

        return this.stackHandler.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = this.stackHandler.getSlotLimit(slot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        this.canInsert == null || this.canInsert.invoke(slot, stack, this.direction)

    companion object {
        fun create(
            stackHandler: StackHandler,
            directions: Array<Direction>,
            canInsert: ((Int, ItemStack, Direction) -> Boolean)?,
            canExtract: ((Int, Direction) -> Boolean)?
        ): Array<LazyOptional<IItemHandlerModifiable>?> {
            val iItemArray: Array<LazyOptional<IItemHandlerModifiable>?> = arrayOfNulls(directions.size)

            for (i in directions.indices) {
                val direction = directions[i]
                iItemArray[i] = LazyOptional.of {
                    SidedItemHandlerModifiable(
                        stackHandler,
                        direction,
                        canInsert,
                        canExtract
                    )
                }
            }

            return iItemArray
        }
    }
}