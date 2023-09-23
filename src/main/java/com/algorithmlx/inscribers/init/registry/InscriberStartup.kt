package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.api.*
import com.algorithmlx.inscribers.compact.CompactInitializer
import com.algorithmlx.inscribers.init.config.InscribersConfig
import com.algorithmlx.inscribers.network.InscribersNetwork

object InscriberStartup {
    @JvmStatic
    @Synchronized
    fun init() {
        InscribersNetwork.messageRegister()
        Register.init()
        CompactInitializer.init()
        makeConfig(side = "common", spec = InscribersConfig.spec)
    }
}