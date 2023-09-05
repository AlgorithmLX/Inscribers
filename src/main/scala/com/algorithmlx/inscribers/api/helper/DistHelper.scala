package com.algorithmlx.inscribers.api.helper

import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.thread.SidedThreadGroups
import net.minecraftforge.fml.loading.FMLEnvironment

object DistHelper {
  def isLogicalClient(level: World): Boolean = level.isClientSide

  def isLogicalServer(level: World): Boolean = !level.isClientSide

  def isLogicalClient: Boolean = Thread.currentThread().getThreadGroup == SidedThreadGroups.CLIENT

  def isLogicalServer: Boolean = Thread.currentThread().getThreadGroup == SidedThreadGroups.SERVER

  def isPhysicalClient: Boolean = FMLEnvironment.dist.isClient

  def isPhysicalServer: Boolean = FMLEnvironment.dist.isDedicatedServer

  def getPhysicalClient: Dist = Dist.CLIENT

  def getPhysicalServer: Dist = Dist.DEDICATED_SERVER
}
