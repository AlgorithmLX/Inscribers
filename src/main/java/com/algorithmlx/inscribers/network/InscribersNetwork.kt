package com.algorithmlx.inscribers.network

import com.algorithmlx.inscribers.network.packet.SDirectionPack
import com.algorithmlx.inscribers.reloc
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.PacketDistributor
import net.minecraftforge.fml.network.simple.SimpleChannel

object InscribersNetwork {
    private lateinit var simpleChannel: SimpleChannel
    private var id: Int = 0
    private var version = "1.0"

    @Synchronized
    fun messageRegister() {
        val localChannel = NetworkRegistry.newSimpleChannel(
            reloc("network"),
            ::version,
            version::equals,
            version::equals
        )
        this.simpleChannel = localChannel

        localChannel.messageBuilder(SDirectionPack::class.java, nextId(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(SDirectionPack::decode)
            .encoder(SDirectionPack::encode)
            .consumer(SDirectionPack::handle)
            .add()
    }

    fun <MSG> sendToServer(message: MSG) {
        this.simpleChannel.sendToServer(message)
    }

    fun <MSG> sendToPlayer(message: MSG, player: ServerPlayerEntity) {
        this.simpleChannel.send(PacketDistributor.PLAYER.with { player }, message)
    }

    fun nextId(): Int {
        id++
        return id
    }
}