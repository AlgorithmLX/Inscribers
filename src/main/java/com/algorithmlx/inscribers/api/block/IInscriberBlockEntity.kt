package com.algorithmlx.inscribers.api.block

import com.algorithmlx.inscribers.api.handler.StackHandler
import com.algorithmlx.inscribers.api.menu
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.energy.IEnergyStorage

interface IInscriberBlockEntity: ITickableTileEntity, INamedContainerProvider {
    fun getInscriber(): IInscriber

    fun getCraftingInventory(): StackHandler

    fun getEnergyStorage(): IEnergyStorage

    private fun getEnergyLazy(): LazyOptional<IEnergyStorage> = LazyOptional.of(this::getEnergyStorage)

    fun <T> getEnergyCapability(isRemoved: Boolean, cap: Capability<T>, or: LazyOptional<T>): LazyOptional<T> =
        if (!isRemoved && cap == CapabilityEnergy.ENERGY)
            CapabilityEnergy.ENERGY.orEmpty(cap, this.getEnergyLazy())
        else or

    override fun getDisplayName(): ITextComponent = menu("inscriber")
}