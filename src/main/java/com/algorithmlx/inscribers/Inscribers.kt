package com.algorithmlx.inscribers

import com.algorithmlx.inscribers.init.registry.InscriberClientSetup
import com.algorithmlx.inscribers.init.registry.InscriberStartup
import net.minecraftforge.fml.common.Mod

@Mod(ModId)
class Inscribers {
    init {
        LOGGER.info("Hello Minecraft World! (Or not?)")
        InscriberStartup.init()
        InscriberClientSetup.setupClient()
    }
}