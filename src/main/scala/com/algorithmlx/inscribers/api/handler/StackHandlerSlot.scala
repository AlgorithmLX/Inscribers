package com.algorithmlx.inscribers.api.handler

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraftforge.items.SlotItemHandler

class StackHandlerSlot(
  private val handler: StackHandler,
  private val index: Int,
  x: Int,
  y: Int
) extends SlotItemHandler(handler, index, x, y) {
  override def mayPickup(playerIn: PlayerEntity): Boolean =
    !this.handler.extract(this.index, 1, simulate = true).isEmpty

  override def remove(amount: Int): ItemStack = this.handler.extract(this.index, amount, simulate = false)
}

