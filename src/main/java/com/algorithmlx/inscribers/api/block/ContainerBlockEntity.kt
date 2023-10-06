package com.algorithmlx.inscribers.api.block

import com.algorithmlx.inscribers.api.handler.StackHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

abstract class ContainerBlockEntity(blockEntity: TileEntityType<*>): OpenBlockEntity(blockEntity) {
    protected val capabilityItemLazy: LazyOptional<IItemHandler> = LazyOptional.of(::getInv)

    abstract fun getInv(): StackHandler

    override fun load(pState: BlockState, pCompound: CompoundNBT) {
        super.load(pState, pCompound)
        this.getInv().deserializeNBT(pCompound)
    }

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        super.save(pCompound)
        pCompound.merge(this.getInv().serializeNBT())
        return pCompound
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (!this.isRemoved && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.capabilityItemLazy)

        return super.getCapability(cap, side)
    }

    fun usedByPlayer(player: PlayerEntity): Boolean {
        val pos = this.blockPos
        return player.distanceToSqr(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64
    }
}