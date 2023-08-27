package com.algorithmlx.inscribers.menu

import com.algorithmlx.inscribers.api.handler.StackHandler
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

class InscriberContainerMenu(
  `type`: ContainerType[_],
  windowId: Int,
  inventory: PlayerInventory,
  private val usedByPlayer: PlayerEntity => Boolean,
  handler: StackHandler,
  private val blockEntity: InscriberBlockEntity,
  private val pos: BlockPos
) extends Container(`type`, windowId) {

  override def stillValid(player : PlayerEntity): Boolean = this.usedByPlayer.apply(player)

  override def quickMoveStack(player : PlayerEntity, index : Int): ItemStack = ItemStack.EMPTY

  def getPos: BlockPos = this.pos

  def getBlockEntity: InscriberBlockEntity = this.blockEntity
}
