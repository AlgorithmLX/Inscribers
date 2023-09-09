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

    fun qb(xMin: Double, yMin: Double, zMin: Double, xMax: Double, yMax: Double, zMax: Double): VoxelHelper {
        val shape = Block.box(xMin, yMin, zMin, xMax, yMax, zMax)
        return this.shape(shape)
    }

    fun of(): VoxelShape = this.last!!

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