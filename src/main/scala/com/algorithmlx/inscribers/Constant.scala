package com.algorithmlx.inscribers

import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.{LogManager, Logger}

object Constant {
  val LOGGER: Logger = LogManager.getLogger("Inscribers")
  final val ModId = "inscribers"
  def reloc(id: String): ResourceLocation = new ResourceLocation(ModId, id)
}
