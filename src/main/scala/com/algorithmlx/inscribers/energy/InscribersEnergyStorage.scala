package com.algorithmlx.inscribers.energy

import net.minecraftforge.energy.EnergyStorage

//noinspection ScalaUnusedSymbol
class InscribersEnergyStorage(capacity: Int, maxReceive: Int, maxExtract: Int, inscriberEnergy: Int, runnable: Runnable)
  extends EnergyStorage(capacity, maxReceive, maxExtract, inscriberEnergy) {

  def this(capacity: Int, maxReceive: Int, maxExtract: Int, runnable: Runnable) = {
    this(capacity, maxReceive, maxExtract, 0, runnable)
  }

  def this(capacity: Int, runnable: Runnable) = {
    this(capacity, capacity, capacity, runnable)
  }

  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    val received = super.receiveEnergy(maxReceive, simulate)

    if (!simulate && received != 0 && this.runnable != null) this.runnable.run()

    received
  }

  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    val extracted = super.extractEnergy(maxExtract, simulate)

    if (!simulate && extracted != 0 && this.runnable != null) this.runnable.run()

    extracted
  }

  def setStored(energy: Int): Unit = {
    this.energy = energy
    
    if (runnable != null) this.runnable.run()
  }
}
