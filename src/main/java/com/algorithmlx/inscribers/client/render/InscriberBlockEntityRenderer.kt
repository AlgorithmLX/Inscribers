package com.algorithmlx.inscribers.client.render

import com.algorithmlx.inscribers.api.renderItem
import com.algorithmlx.inscribers.block.entity.StandaloneInscriberBlockEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.item.BlockItem

class InscriberBlockEntityRenderer(pContext: TileEntityRendererDispatcher) : TileEntityRenderer<StandaloneInscriberBlockEntity>(
    pContext
) {
    private var isRendered = false
    private var isRenderedEmpty = false

    override fun render(
        pBlockEntity: StandaloneInscriberBlockEntity,
        pPartialTicks: Float,
        pPoseStack: MatrixStack,
        pBuffer: IRenderTypeBuffer,
        pCombinedLight: Int,
        pCombinedOverlay: Int
    ) {
        val inv = pBlockEntity.getInv()

        for (i in 0 until 6) {
            for (j in 0 until 6) {
                val stack = inv.getStackInSlot(i * j)
                if (!stack.isEmpty) {
                    pPoseStack.renderItem(
                        stack,
                        11.25 - i * 1.5,
                        4.8,
                        11.3 - j * 1.5,
                        pCombinedLight,
                        pCombinedOverlay,
                        pBuffer
                    )
                }
            }
        }

        val resultStack = inv.getStackInSlot(0)

        if (!resultStack.isEmpty) {
            val scale: Float = if (resultStack.item is BlockItem) 2F else 1.75F

            for (i in 1 until 36) {
                val stacks = inv.getStackInSlot(i)
                if (stacks.isEmpty) {
                    if (this.isRendered) this.isRendered = false
                    else {
                        if (!this.isRenderedEmpty) {
                            pPoseStack.renderItem(stacks, scale, 7.0, 4.8, 7.0, pCombinedLight, pCombinedOverlay, pBuffer)
                            this.isRenderedEmpty = true
                        }
                    }
                } else {
                    if (this.isRenderedEmpty) this.isRenderedEmpty = false
                    else {
                        if (!this.isRendered) {
                            pPoseStack.renderItem(stacks, scale, 7.0, 4.0, 7.0, pCombinedLight, pCombinedOverlay, pBuffer)
                            this.isRendered = true
                        }
                    }
                }
            }
        }
    }
}