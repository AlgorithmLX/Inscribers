package com.algorithmlx.inscribers.api.helper

import net.minecraft.block.Block
import net.minecraft.util.math.shapes.{VoxelShape, VoxelShapes}

class VoxelBuilder private() {
  private var left: VoxelShape = _
  private var last: VoxelShape = _

  def shape(shape: VoxelShape): VoxelBuilder = {
    if (left == null) left = shape
    else {
      val newShape = VoxelShapes.or(left, shape)
      last = if (last != null) VoxelShapes.or(last, newShape) else newShape
      left = null
    }

    this
  }

  def qb(xMin: Double, yMin: Double, zMin: Double, xMax: Double, yMax: Double, zMax: Double): VoxelBuilder = {
    val shape = Block.box(xMin, yMin, zMin, xMax, yMax, zMax)
    this.shape(shape)
  }

  def of: VoxelShape = this.last
}

object VoxelBuilder {
  def builder: VoxelBuilder = {
    new VoxelBuilder
  }

  def from(shapes: VoxelShape*): VoxelBuilder = {
    val builder = new VoxelBuilder
    for (shape <- shapes) {
      builder.shape(shape)
    }
    builder
  }
}
