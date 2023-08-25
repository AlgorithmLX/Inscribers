package com.algorithmlx.inscribers

import com.algorithmlx.inscribers.Constant._
import com.algorithmlx.inscribers.init.Register
import net.minecraftforge.fml.common.Mod

@Mod(ModId)
class Inscribers {
  LOGGER.info("Hello Minecraft World! (Or not?)")
  Register.init()
}
