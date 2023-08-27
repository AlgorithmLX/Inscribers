package com.algorithmlx.inscribers.api.handler

import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandlerModifiable

// Author: BlakeBr0 (https://github.com/BlakeBr0)
// Translate to Scala: AlgorithmLX
object SidedItemHandlerModifiable {
  def create(
              stackHandler: StackHandler,
              directions: Array[Direction],
              canInsert: (Int, ItemStack, Direction) => Boolean,
              canExtract: (Int, Direction) => Boolean
            ): Array[LazyOptional[IItemHandlerModifiable]] = {
    val iItemArray = new Array[LazyOptional[IItemHandlerModifiable]](directions.length)

    var i = 0

    while(i < directions.length) {
      val direction = directions(i)
      iItemArray(i) = LazyOptional.of(()=> new Invoker(stackHandler, direction, canInsert, canExtract))
      i += 1
    }

    iItemArray
  }

  class Invoker(
    stackHandler: StackHandler,
    direction: Direction,
    canInsert: (Int, ItemStack, Direction) => Boolean,
    canExtract: (Int, Direction) => Boolean
  ) extends IItemHandlerModifiable {
    override def setStackInSlot(slot: Int, stack: ItemStack): Unit = this.stackHandler.setStackInSlot(slot, stack)

    override def getSlots: Int = this.stackHandler.getSlots

    override def getStackInSlot(slot: Int): ItemStack = this.stackHandler.getStackInSlot(slot)

    override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = {
      if (stack.isEmpty) return ItemStack.EMPTY

      if (this.isItemValid(slot, stack)) return stack

      this.stackHandler.insertItem(slot, stack, simulate)
    }

    override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = {
      if (this.canExtract != null && !this.canExtract.apply(slot, this.direction)) return ItemStack.EMPTY

      this.stackHandler.extractItem(slot, amount, simulate)
    }

    override def getSlotLimit(slot: Int): Int = {
      this.stackHandler.getSlotLimit(slot)
    }

    override def isItemValid(slot: Int, stack: ItemStack): Boolean = {
      this.canInsert == null || this.canInsert.apply(slot, stack, this.direction)
    }
  }
}
