package com.algorithmlx.inscribers.menu

import com.algorithmlx.inscribers.api.handler.{StackHandler, StackHandlerSlot}
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.container.InscriberCraftingContainer
import com.algorithmlx.inscribers.container.slot.InscriberResultSlot
import com.algorithmlx.inscribers.init.Register
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.{CraftResultInventory, IInventory}
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos

class InscriberContainerMenu(
  `type`: ContainerType[_],
  windowId: Int,
  inventory: PlayerInventory,
  private val usedByPlayer: PlayerEntity => Boolean,
  private val blockEntity: InscriberBlockEntity,
  private val pos: BlockPos
) extends Container(`type`, windowId) {
  private val result: IInventory = new CraftResultInventory()
  private val craftInventory: IInventory = new InscriberCraftingContainer(this, blockEntity.getInv(), 6)

  def this(`type`: ContainerType[_], id: Int, inventory: PlayerInventory, pack: PacketBuffer) = {
    this(
      `type`,
      id,
      inventory,
      _ => false,
      Register.INSCRIBER_BLOCK_ENTITY.create().asInstanceOf[InscriberBlockEntity],
      pack.readBlockPos()
    )
  }

  this.addSlot(new InscriberResultSlot(this, craftInventory, this.result, 0, 79, 163))

  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 1, 34, 18))
  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 2, 52, 18))
  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 3, 70, 18))
  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 4, 88, 18))
  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 5, 106, 18))
  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 6, 34, 18))
  this.addSlot(new StackHandlerSlot(this.getBlockEntity.getInv(), 7, 124, 18))

  override def stillValid(player : PlayerEntity): Boolean = this.usedByPlayer.apply(player)

  override def quickMoveStack(player : PlayerEntity, index : Int): ItemStack = ItemStack.EMPTY

  def getPos: BlockPos = this.pos
  def getBlockEntity: InscriberBlockEntity = this.blockEntity
}
