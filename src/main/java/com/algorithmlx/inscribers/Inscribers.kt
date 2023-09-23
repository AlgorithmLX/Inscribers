package com.algorithmlx.inscribers

import com.algorithmlx.inscribers.api.makeConfig
import com.algorithmlx.inscribers.compact.CompactInitializer
import com.algorithmlx.inscribers.init.config.InscribersConfig
import com.algorithmlx.inscribers.init.registry.InscriberClientSetup
import com.algorithmlx.inscribers.init.registry.InscriberStartup
import com.algorithmlx.inscribers.init.registry.Register
import com.algorithmlx.inscribers.network.InscribersNetwork
import net.minecraftforge.fml.common.Mod

@Mod(ModId)
class Inscribers {
    init {
        LOGGER.info("Hello Minecraft World! (Or not?)")
        InscriberStartup.init()
        InscriberClientSetup.setupClient()
    }
}