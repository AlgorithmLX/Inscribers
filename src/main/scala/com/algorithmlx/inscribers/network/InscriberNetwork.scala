package com.algorithmlx.inscribers.network

import com.algorithmlx.inscribers.Constant.reloc
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.fml.network.{NetworkRegistry, PacketDistributor}
import net.minecraftforge.fml.network.simple.SimpleChannel

object InscriberNetwork {
  private var simpleChannel: SimpleChannel = _
  private var id: Int = 0
  private val version = "1.0"

  def messageRegister(): Unit = this.synchronized {
    this.simpleChannel = NetworkRegistry.newSimpleChannel(
      reloc("network"),
      () => version,
      obj => version.equals(obj),
      obj => version.equals(obj)
    )
  }

  def sendToServer[MSG](message: MSG): Unit = simpleChannel.sendToServer(message)

  def sendToPlayer[MSG](message: MSG, player: ServerPlayerEntity): Unit = simpleChannel.send(
    PacketDistributor.PLAYER.`with`(()=> player),
    message
  )

  private def nextId(): Int = {
    id += 1
    id
  }
}
