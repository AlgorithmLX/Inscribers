package com.algorithmlx.inscribers.api.helper

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BlockEntityHelper {
  def playerDispatch(blockEntity: TileEntity): Unit = {
    val level = blockEntity.getLevel
    if (level == null) return
    val packet = blockEntity.getUpdatePacket
    if (packet == null) return
    val players = level.players
    val pos = blockEntity.getBlockPos

    players.forEach {
      case player: ServerPlayerEntity =>
        if (isPlayerNearby(player.getX, player.getZ, pos.getX + 0.5, pos.getZ + 0.5)) player.connection.send(packet)
      case _ =>
    }
  }

  def playerDispatch(world: World, x: Int, y: Int, z: Int): Unit = {
    val tile = world.getBlockEntity(new BlockPos(x, y, z))
    if (tile != null) playerDispatch(tile)
  }

  private def isPlayerNearby(x1: Double, z1: Double, x2: Double, z2: Double) = Math.hypot(x1 - x2, z1 - z2) < 64
}
