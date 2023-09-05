package com.algorithmlx.inscribers.compact

import net.minecraftforge.fml.ModList

object CompactInitializer {
  def init(): Unit = {
  }

  def isGeckolibLoaded: Boolean = this.modLoad("geckolib")

  def modLoad(id: String): Boolean = ModList.get().isLoaded(id)
}
