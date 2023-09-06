package com.algorithmlx.inscribers.network.packet

import com.algorithmlx.inscribers.server.InscriberDirectionSettingsServer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent

import java.util.function.Supplier

class SDirectionPack private() {
  final var data = 0

  def this(data: Int) = {
    this()
    this.data = data
  }

  def this(buf: PacketBuffer) = {
    this()
    this.data = buf.readInt()
  }

  def encode(buf: PacketBuffer): Unit = {
    buf.writeInt(this.data)
  }

  def handle(ctx: Supplier[NetworkEvent.Context]): Unit = {
    val context = ctx.get()
    context.enqueueWork(()=> InscriberDirectionSettingsServer.setData(this.data))
  }
}

object SDirectionPack {
  def decode(buf: PacketBuffer): SDirectionPack = new SDirectionPack(buf)
}