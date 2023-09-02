package com.algorithmlx.inscribers.client.render

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.client.render.InscriberBlockEntityRenderer.laser
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.RenderMaterial
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.tileentity.{TileEntityRenderer, TileEntityRendererDispatcher}
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.util.ResourceLocation

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

object InscriberBlockEntityRenderer {
  val laser: RenderMaterial = makeMCMaterial("block/beacon")
  val laserCore: RenderMaterial = makeMCMaterial("block/gold_block")
  val laserCable: RenderMaterial = makeMCMaterial("block/redstone_block")
  val laserRod: RenderMaterial = makeMCMaterial("block/diamond_block")
  val laserRodDown: RenderMaterial = makeMCMaterial("block/chiseled_polished_blackstone")

  val laserElevatorIn: RenderMaterial = makeMaterial("block/inscriber")
  val laserElevatorOut: RenderMaterial = makeMCMaterial("block/iron_block")
  val laserElevatorRod: RenderMaterial = makeMCMaterial("block/smithing_table_top")

  val energyCore: RenderMaterial = makeMCMaterial("block/lapis_block")
  val inscriberCentral: RenderMaterial = makeMaterial("block/inscriber")
  val inscriberGlass: RenderMaterial = makeMCMaterial("block/black_stained_glass")
  val inscriberBolt: RenderMaterial = makeMCMaterial("block/chiseled_polished_blackstone")

  private def makeMCMaterial(path: String): RenderMaterial = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation(path))
  private def makeMaterial(path: String): RenderMaterial = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, reloc(path))
}