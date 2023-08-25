package com.algorithmlx.inscribers.init

import net.minecraftforge.common.ForgeConfigSpec

object InscribersConfig {
  val BUILDER: ForgeConfigSpec.Builder = new ForgeConfigSpec.Builder

  BUILDER.push("Energy settings")

  val INSCRIBER_CAPACITY: ForgeConfigSpec.IntValue = BUILDER.defineInRange("inscriber_capacity", 1000000, 1000, Int.MaxValue)

  BUILDER.pop

  val SPEC: ForgeConfigSpec = BUILDER.build()
}
