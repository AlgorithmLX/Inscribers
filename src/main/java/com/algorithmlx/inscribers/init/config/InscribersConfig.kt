package com.algorithmlx.inscribers.init.config

import net.minecraftforge.common.ForgeConfigSpec

object InscribersConfig {
    private val builder: ForgeConfigSpec.Builder = ForgeConfigSpec.Builder()

    init {
        builder.push("Standard Inscriber")
        builder.push("Energy Settings")
    }

    val inscriberCapacity: ForgeConfigSpec.IntValue = builder.defineInRange("inscriber_capacity", 1000000, 1000, Int.MAX_VALUE)
    val inscriberPowerInsert: ForgeConfigSpec.IntValue = builder.defineInRange("inscriber_insert", 10000, 10, Int.MAX_VALUE)

    init {
        builder.pop()
    }

    val spec: ForgeConfigSpec = builder.build()
}