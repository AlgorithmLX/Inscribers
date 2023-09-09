package com.algorithmlx.inscribers

import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.*

val LOGGER: Logger = LogManager.getLogger("Inscribers")
const val ModId = "inscriber"
fun reloc(id: String): ResourceLocation = ResourceLocation(ModId, id)