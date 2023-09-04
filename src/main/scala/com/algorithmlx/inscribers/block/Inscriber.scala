package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.api.helper.VoxelBuilder
import com.algorithmlx.inscribers.compact.CompactInitializer
import com.algorithmlx.inscribers.init.registry.Register
import net.minecraft.block.AbstractBlock.Properties
import net.minecraft.block.{Block, BlockRenderType, BlockState}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.{ISelectionContext, VoxelShape}
import net.minecraft.world.IBlockReader
//noinspection ScalaDeprecation
class Inscriber(properties: Properties) extends Block(properties) {
  private val voxel = VoxelBuilder.builder
    .qb(4, 0, 4, 12, 1, 12)
    .qb(7, 0, 14, 9, 15, 16)
    .qb(2, 1, 2, 14, 4, 14)
    .qb(12, 4, 12.2, 12.5, 5, 12.7)
    .qb(3, 4.3, 3.2, 12.9, 4.8, 12.925)
    .qb(3.5, 4, 12.025, 4, 5, 12.525)
    .qb(12, 4, 3.5, 12.5, 5, 4)
    .qb(3.525, 4, 3.5, 4.025, 5, 4)
    .qb(2, 14.5, 12, 14, 15, 11.775)
    .qb(2, 12.5, 11.273, 14, 15, 11.775)
    .qb(2, 12.5, 11.775, 14, 15, 12.275)
    .qb(7, 15, 11.275, 9, 15.45, 14)
    .qb(7, 12.5, 16, 9, 15, 16.8)
    .qb(7, 12.05, 11.275, 9, 12.5, 14)
    .qb(7, 12.6, 12.276, 9, 16, 14)
    .qb(6.275, 12.5, 12.245, 7, 15, 16.8)
    .qb(13.5, 13, 11.275, 14, 14.5, 11.775)
    .qb(2, 13, 11.275, 2.5, 14.5, 11.775)
    .qb(9, 12.5, 12.275, 9.725, 15, 16.8)
    .qb(7.5, 12.1, 4.05, 8.5, 13.1, 5.05)
    .qb(7, 10.85, 3.55, 9, 12.85, 5.55)
    .qb(7.75, 9.85, 4.275, 8.25, 11.85, 4.775)
    .qb(7.25, 13, 3.275, 8.75, 14.5, 11.775)
    .of

  override def getShape(state: BlockState, reader: IBlockReader, pos: BlockPos, select: ISelectionContext): VoxelShape = this.voxel

  override def hasTileEntity(state: BlockState): Boolean = true

  override def createTileEntity(state: BlockState, world: IBlockReader): TileEntity = new InscriberBlockEntity(Register.INSCRIBER_BLOCK_ENTITY.get())

  override def getRenderShape(p_149645_1_ : BlockState): BlockRenderType = {
//    if (CompactInitializer.isGeckolibLoaded) {
//      return BlockRenderType.ENTITYBLOCK_ANIMATED
//    }

    super.getRenderShape(p_149645_1_)
  }
}
