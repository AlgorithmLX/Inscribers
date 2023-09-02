package com.algorithmlx.inscribers.compact

import com.algorithmlx.inscribers.compact.geckolib.GeckoStarter
import net.minecraftforge.fml.ModList

object CompactInitializer {
  def init(): Unit = {
    if (this.isGeckolibLoaded) GeckoStarter.init()
  }

  def isGeckolibLoaded: Boolean = this.modLoad("geckolib")

  def modLoad(id: String): Boolean = ModList.get().isLoaded(id)
}
