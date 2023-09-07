package com.algorithmlx.inscribers.network.packet

import com.algorithmlx.inscribers.server.InscriberDirectionSettingsServer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent

import java.util.function.Supplier

//noinspection ScalaUnusedSymbol
class SDirectionPack private() {
  final var data = 0
  final var enabled = false

  def this(data: Int, enabled: Boolean) = {
    this()
    this.data = data
    this.enabled = enabled
  }

  def this(buf: PacketBuffer) = {
    this()
    this.data = buf.readInt()
    this.enabled = buf.readBoolean()
  }

  def encode(buf: PacketBuffer): Unit = {
    buf.writeInt(this.data)
    buf.writeBoolean(this.enabled)
  }

  def handle(ctx: Supplier[NetworkEvent.Context]): Unit = {
    val context = ctx.get()
    context.enqueueWork(()=> {
      InscriberDirectionSettingsServer.setData(this.data)
      InscriberDirectionSettingsServer.setEnabled(this.enabled)
    })
  }
}

object SDirectionPack {
  def decode(buf: PacketBuffer): SDirectionPack = new SDirectionPack(buf)
}