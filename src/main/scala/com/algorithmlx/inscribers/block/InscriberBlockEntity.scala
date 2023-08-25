package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.energy.InscribersEnergyStorage
import com.algorithmlx.inscribers.init.InscribersConfig
import net.minecraft.tileentity.{ITickableTileEntity, TileEntity, TileEntityType}
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional

class InscriberBlockEntity(`type`: TileEntityType[TileEntity]) extends TileEntity(`type`) with ITickableTileEntity {
  val energy: InscribersEnergyStorage = new InscribersEnergyStorage(InscribersConfig.INSCRIBER_CAPACITY.get(), ()=>{})

  override def tick(): Unit = {

  }

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    super.getCapability(cap, side)
  }
}
