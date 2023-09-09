package com.algorithmlx.inscribers.api.helper

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.hypot

object BlockEntityHelper {
    fun playerDispatch(blockEntity: TileEntity) {
        val level = blockEntity.level ?: return
        val packet = blockEntity.updatePacket ?: return
        val players = level.players()
        val pos = blockEntity.blockPos

        players.forEach { player ->
            if (player is ServerPlayerEntity)
                if (isPlayerNearby(player.x, player.z, pos.x + 0.5, pos.z + 0.5))
                    player.connection.send(packet)
        }
    }

    fun playerDispatch(level: World, x: Int, y: Int, z: Int) {
        val blockEntity = level.getBlockEntity(BlockPos(x, y, z))
        if (blockEntity != null) playerDispatch(blockEntity)
    }

    private fun isPlayerNearby(x1: Double, z1: Double, x2: Double, z2: Double) = hypot(x1 - x2, z1 - z2) < 64
}