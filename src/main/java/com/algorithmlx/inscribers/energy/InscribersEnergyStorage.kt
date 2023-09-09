package com.algorithmlx.inscribers.energy

import net.minecraftforge.energy.EnergyStorage

class InscribersEnergyStorage(
    capacity: Int,
    maxReceive: Int = capacity,
    maxExtract: Int = capacity,
    inscriberEnergy: Int = 0,
    private val runnable: Runnable?
): EnergyStorage(capacity, maxReceive, maxExtract, inscriberEnergy) {
    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val received = super.receiveEnergy(maxReceive, simulate)

        if (!simulate && received != 0 && this.runnable != null) this.runnable.run()

        return received
    }

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        val extracted = super.extractEnergy(maxExtract, simulate)

        if (!simulate && extracted != 0 && this.runnable != null) this.runnable.run()

        return extracted
    }

    fun setStored(energy: Int) {
        this.energy = energy

        if (runnable != null) this.runnable.run()
    }
}