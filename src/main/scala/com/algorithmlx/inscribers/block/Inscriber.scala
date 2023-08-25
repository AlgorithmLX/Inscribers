package com.algorithmlx.inscribers.block

import net.minecraft.block.AbstractBlock.Properties
import net.minecraft.block.{Block, BlockState}
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

class Inscriber(properties: Properties) extends Block(properties) {
  override def hasTileEntity(state: BlockState): Boolean = true

  override def createTileEntity(state: BlockState, world: IBlockReader): TileEntity = {
    super.createTileEntity(state, world)
  }

}
