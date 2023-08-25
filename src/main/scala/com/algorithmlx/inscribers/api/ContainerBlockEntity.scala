package com.algorithmlx.inscribers.api

import com.algorithmlx.inscribers.api.helper.BlockEntityHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.{TileEntity, TileEntityType}

class ContainerBlockEntity(blockEntityType: TileEntityType[_]) extends TileEntity(blockEntityType) {
  override def getUpdatePacket: SUpdateTileEntityPacket = new SUpdateTileEntityPacket(this.getBlockPos, -1, this.getUpdateTag)

  override def onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket): Unit = this.load(this.getBlockState, pkt.getTag)

  override def getUpdateTag: CompoundNBT = this.save(new CompoundNBT())

  def changeX(): Unit = {
    super.setChanged()
    BlockEntityHelper.playerDispatch(this)
  }
}
