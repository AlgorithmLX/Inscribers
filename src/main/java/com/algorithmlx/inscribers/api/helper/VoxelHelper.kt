package com.algorithmlx.inscribers.api.helper

import net.minecraft.block.Block
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes

class VoxelHelper private constructor() {
    private var left: VoxelShape? = null
    private var last: VoxelShape? = null

    fun shape(shape: VoxelShape): VoxelHelper {
        if (this.left == null) left = shape
        else {
            val newShape = VoxelShapes.or(left!!, shape)
            last = if (last != null) VoxelShapes.or(last!!, newShape) else newShape
            left = null
        }

        return this
    }

    fun qb(xMin: Number, yMin: Number, zMin: Number, xMax: Number, yMax: Number, zMax: Number): VoxelHelper {
        val shape = Block.box(xMin.toDouble(), yMin.toDouble(), zMin.toDouble(), xMax.toDouble(), yMax.toDouble(), zMax.toDouble())
        return this.shape(shape)
    }

    fun of(): VoxelShape = if (this.last != null)
        this.last!!
    else
        Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)

    companion object {
        @JvmStatic
        fun builder(): VoxelHelper {
            return VoxelHelper()
        }

        @JvmStatic
        fun from(vararg shapes: VoxelShape): VoxelHelper {
            val builder = VoxelHelper()

            for (shape in shapes) {
                builder.shape(shape)
            }

            return builder
        }
    }
}