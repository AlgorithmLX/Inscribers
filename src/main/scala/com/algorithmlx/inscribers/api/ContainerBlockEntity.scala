package com.algorithmlx.inscribers.api

import com.algorithmlx.inscribers.api.handler.StackHandler
import com.algorithmlx.inscribers.api.helper.BlockEntityHelper
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandler}

abstract class ContainerBlockEntity(blockEntityType: TileEntityType[_]) extends TileEntity(blockEntityType) {
  private val cap: LazyOptional[IItemHandler] = LazyOptional.of(()=> this.getInv())

  def getInv(): StackHandler

  override def getUpdatePacket: SUpdateTileEntityPacket = new SUpdateTileEntityPacket(this.getBlockPos, -1, this.getUpdateTag)

  override def onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket): Unit = this.load(this.getBlockState, pkt.getTag)

  override def getUpdateTag: CompoundNBT = this.save(new CompoundNBT())

  override def load(state : BlockState, tag : CompoundNBT): Unit = {
    super.load(state, tag)
    this.getInv().deserializeNBT(tag)
  }

  override def save(tag : CompoundNBT): CompoundNBT = {
    super.save(tag)
    tag.merge(this.getInv().serializeNBT())
  }

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (!this.isRemoved && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
      return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.cap)
    super.getCapability(cap, side)
  }

  def changeX(): Unit = {
    super.setChanged()
    BlockEntityHelper.playerDispatch(this)
  }

  def usedByPlayer(player: PlayerEntity): Boolean = {
    val pos = this.getBlockPos
    player.distanceToSqr(pos.getX + 0.5, pos.getY + 0.5, pos.getZ + 0.5) <= 64
  }
}
