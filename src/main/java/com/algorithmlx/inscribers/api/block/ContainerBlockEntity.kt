package com.algorithmlx.inscribers.api.block

import com.algorithmlx.inscribers.api.handler.StackHandler
import com.algorithmlx.inscribers.api.helper.BlockEntityHelper
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

abstract class ContainerBlockEntity(blockEntityType: TileEntityType<*>): TileEntity(blockEntityType) {
    protected val itemCap: LazyOptional<StackHandler> = LazyOptional.of(::getInv)

    abstract fun getInv(): StackHandler

    override fun getUpdatePacket(): SUpdateTileEntityPacket? = SUpdateTileEntityPacket(this.blockPos, -1, this.updateTag)

    override fun onDataPacket(net: NetworkManager?, pkt: SUpdateTileEntityPacket?) {
        pkt?.tag?.let { this.load(this.blockState, it) }
    }

    override fun getUpdateTag(): CompoundNBT = this.save(CompoundNBT())

    override fun load(state: BlockState, tag: CompoundNBT) {
        super.load(state, tag)
        this.getInv().deserializeNBT(tag)
    }

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        super.save(pCompound)
        return pCompound.merge(this.getInv().serializeNBT())
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (!this.isRemoved && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.itemCap.cast())

        return super.getCapability(cap, side)
    }

    fun change() {
        super.setChanged()
        BlockEntityHelper.playerDispatch(this)
    }

    fun usedByPlayer(player: PlayerEntity): Boolean {
        val pos = this.blockPos
        return player.distanceToSqr(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64
    }
}