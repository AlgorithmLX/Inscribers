package com.algorithmlx.inscribers.api.block

import com.algorithmlx.inscribers.api.menu
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.text.ITextComponent

interface IInscriberBlockEntity: ITickableTileEntity, INamedContainerProvider {
    fun getInscriber(): IInscriber

    override fun getDisplayName(): ITextComponent = menu("inscriber")


}