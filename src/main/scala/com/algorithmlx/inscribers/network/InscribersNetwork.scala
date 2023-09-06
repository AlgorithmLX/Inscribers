package com.algorithmlx.inscribers.network

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.network.packet.SDirectionPack
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.{NetworkDirection, NetworkEvent, NetworkRegistry, PacketDistributor}
import net.minecraftforge.fml.network.simple.SimpleChannel

import java.util.function.Supplier

object InscribersNetwork {
  private var simpleChannel: SimpleChannel = _
  private var id: Int = 0
  private val version = "1.0"

  def messageRegister(): Unit = this.synchronized {
    val localChannel = NetworkRegistry.newSimpleChannel(
      reloc("network"),
      () => version,
      obj => version.equals(obj),
      obj => version.equals(obj)
    )

    this.simpleChannel = localChannel

    localChannel.messageBuilder(classOf[SDirectionPack], nextId(), NetworkDirection.PLAY_TO_SERVER)
      .decoder(buf => new SDirectionPack(buf))
      .encoder((_, buf) => SDirectionPack.decode(buf))
      .consumer((pack: SDirectionPack, evt: Supplier[NetworkEvent.Context]) => pack.handle(evt))
      .add()
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
