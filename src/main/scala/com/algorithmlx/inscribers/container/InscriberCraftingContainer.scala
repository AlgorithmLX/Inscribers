package com.algorithmlx.inscribers.container

import com.algorithmlx.inscribers.api.handler.StackHandler
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.container.Container
import net.minecraft.item.ItemStack

class InscriberCraftingContainer(
  private val inventory: Container,
  private val handler: StackHandler,
  size: Int
) extends CraftingInventory(inventory, size, size) {
  override def getContainerSize: Int = this.handler.getSlots

  override def isEmpty: Boolean = {
    var i = 0

    while (i < this.getContainerSize) {
      if (!this.handler.getStackInSlot(i).isEmpty) return false
      i += 1
    }

    false
  }

  override def getItem(slot : Int): ItemStack = this.handler.getStackInSlot(slot)
}
