package com.algorithmlx.inscribers.api.block

import com.algorithmlx.inscribers.api.helper.BlockEntityHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType

open class OpenBlockEntity(blockEntity: TileEntityType<*>) : TileEntity(blockEntity) {
    override fun getUpdatePacket(): SUpdateTileEntityPacket? =
        SUpdateTileEntityPacket(this.blockPos, -1, this.updateTag)

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket) {
        this.load(this.blockState, pkt.tag)
    }

    override fun getUpdateTag(): CompoundNBT = this.save(CompoundNBT())

    fun change() {
        super.setChanged()
        BlockEntityHelper.playerDispatch(this)
    }
}