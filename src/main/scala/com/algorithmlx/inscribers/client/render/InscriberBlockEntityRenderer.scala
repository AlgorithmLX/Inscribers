package com.algorithmlx.inscribers.client.render

import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.tileentity.{TileEntityRenderer, TileEntityRendererDispatcher}

class InscriberBlockEntityRenderer(ctx: TileEntityRendererDispatcher) extends TileEntityRenderer[InscriberBlockEntity](ctx) {
  override def render(
    blockEntity : InscriberBlockEntity,
    partialTick : Float,
    poseStack : MatrixStack,
    buf : IRenderTypeBuffer,
    hz : Int,
    hz0 : Int
  ): Unit = {
    super.render(blockEntity, partialTick, poseStack, buf, hz, hz0)
  }
}
