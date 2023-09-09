package com.algorithmlx.inscribers

import com.algorithmlx.inscribers.api.makeConfig
import com.algorithmlx.inscribers.compact.CompactInitializer
import com.algorithmlx.inscribers.init.config.InscribersConfig
import com.algorithmlx.inscribers.init.registry.Register
import com.algorithmlx.inscribers.network.InscribersNetwork
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig

@Mod(ModId)
class Inscribers {
    init {
        LOGGER.info("Hello Minecraft World! (Or not?)")
        InscribersNetwork.messageRegister()
        CompactInitializer.init()
        Register.init()
        makeConfig(side = "common", spec = InscribersConfig.spec)
    }
}