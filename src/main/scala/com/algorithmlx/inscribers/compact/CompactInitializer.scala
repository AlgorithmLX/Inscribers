package com.algorithmlx.inscribers.compact

import net.minecraftforge.fml.ModList

//noinspection ScalaUnusedSymbol
object CompactInitializer {
  def init(): Unit = {
  }

  def isGeckolibLoaded: Boolean = this.modLoad("geckolib")

  def modLoad(id: String): Boolean = ModList.get().isLoaded(id)
}