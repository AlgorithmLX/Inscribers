package com.algorithmlx.inscribers.network.packet

import com.algorithmlx.inscribers.server.InscriberDirectionSettings
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class SDirectionPack {
    private val data: Int
    private val enabled: Boolean

    constructor(data: Int, enabled: Boolean) {
        this.data = data
        this.enabled = enabled
    }

    constructor(buf: PacketBuffer) {
        this.data = buf.readInt()
        this.enabled = buf.readBoolean()
    }

    fun encode(buf: PacketBuffer) {
        buf.writeInt(this.data)
        buf.writeBoolean(this.enabled)
    }

    companion object {
        @JvmStatic
        fun decode(buf: PacketBuffer): SDirectionPack = SDirectionPack(buf)
    }

    fun handle(ctx: Supplier<NetworkEvent.Context>) {
        val context = ctx.get()
        context.enqueueWork {
            InscriberDirectionSettings.data = this.data
            InscriberDirectionSettings.enabled = this.enabled
        }
    }
}