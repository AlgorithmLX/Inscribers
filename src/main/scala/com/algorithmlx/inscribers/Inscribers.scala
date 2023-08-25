package com.algorithmlx.inscribers

import com.algorithmlx.inscribers.Constant._
import com.algorithmlx.inscribers.init.{InscribersConfig, Register}
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig

@Mod(ModId)
class Inscribers {
  LOGGER.info("Hello Minecraft World! (Or not?)")
  Register.init()
  ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, InscribersConfig.SPEC, "inscribers/common.toml")
}
