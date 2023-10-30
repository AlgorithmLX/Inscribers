package com.algorithmlx.inscribers.api.handler

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemStackHandler
import org.apache.commons.lang3.ArrayUtils

class StackHandler(size: Int, private val contextChanged: Runnable? = null): ItemStackHandler(size) {
    private var slotSize: MutableMap<Int, Int> = mutableMapOf()
    private var validator: ((Int, ItemStack) -> Boolean)? = null
    private var maxStackSize = 64
    private var outputs: IntArray? = null

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (this.outputs != null && ArrayUtils.contains(this.outputs, slot)) return stack
        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (this.outputs != null && !ArrayUtils.contains(this.outputs, slot)) return ItemStack.EMPTY
        return super.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = if (this.slotSize.containsKey(slot)) this.slotSize[slot]!! else this.maxStackSize

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = this.validator == null || this.validator!!.invoke(slot, stack)

    fun insert(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = super.insertItem(slot, stack, simulate)

    fun extract(slot: Int, amount: Int, simulate: Boolean): ItemStack = super.extractItem(slot, amount, simulate)

    fun getStackList(): NonNullList<ItemStack> = this.stacks

    fun getOutputs(): IntArray = this.outputs!!

    fun setDefaultSlotLimit(size: Int) {
        this.maxStackSize = size
    }

    fun addSlotLimit(slot: Int, size: Int) {
        this.slotSize[slot] = size
    }

    fun setValidator(validator: (Int, ItemStack) -> Boolean) {
        this.validator = validator
    }

    fun setOutputSlots(vararg outputs: Int) {
        this.outputs = outputs
    }

    fun toContainer(): IInventory = Inventory(this.stacks.toTypedArray()[0])

    fun copy(): StackHandler {
        val copied = StackHandler(this.slots, this.contextChanged)
        copied.setDefaultSlotLimit(this.maxStackSize)
        copied.setValidator(this.validator!!)
        copied.setOutputSlots(*this.outputs!!)

        this.slotSize.forEach(copied::addSlotLimit)

        for (i in 0 until this.slots) {
            val stack = this.getStackInSlot(i)
            copied.setStackInSlot(i, stack.copy())
        }

        return copied
    }
}