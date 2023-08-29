package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.init.Register
import net.minecraft.block.AbstractBlock.Properties
import net.minecraft.block.{Block, BlockState}
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

class Inscriber(properties: Properties) extends Block(properties) {
  override def hasTileEntity(state: BlockState): Boolean = true

  override def createTileEntity(state: BlockState, world: IBlockReader): TileEntity = new InscriberBlockEntity(Register.INSCRIBER_BLOCK_ENTITY.get())
}
