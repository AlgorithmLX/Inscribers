package com.algorithmlx.inscribers.init.config

import net.minecraftforge.common.ForgeConfigSpec

object InscribersConfig {
    private val builder: ForgeConfigSpec.Builder = ForgeConfigSpec.Builder()

    init {
        builder.push("Energy settings")
    }

    val inscriberCapacity: ForgeConfigSpec.IntValue = builder.defineInRange("inscriber_capacity", 1000000, 1000, Int.MAX_VALUE)

    init {
        builder.pop()
    }

    val spec: ForgeConfigSpec = builder.build()
}