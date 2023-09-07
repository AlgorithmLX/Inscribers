package com.algorithmlx.inscribers.client.render

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.{ItemCameraTransforms, RenderMaterial}
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.item.{BlockItem, ItemStack}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.animation.TileEntityRendererAnimation

//noinspection DuplicatedCode
class InscriberBlockEntityRenderer(ctx: TileEntityRendererDispatcher) extends TileEntityRendererAnimation[InscriberBlockEntity](ctx) {
  override def render(
    blockEntity : InscriberBlockEntity,
    partialTick : Float,
    poseStack : MatrixStack,
    buf : IRenderTypeBuffer,
    light : Int,
    otherLight : Int
  ): Unit = {
    val inv = blockEntity.getInv

    for (i <- 1 until 36) {
      val stack = inv.getStackInSlot(i)
      if (!stack.isEmpty) {
        for (i <- 0 until 6) {
          for (j <- 0 until 6) {
            this.renderItem(poseStack, stack, 11.25 - 1.5 * i, 11.3 - 1.5 * j, buf, light, otherLight)
          }
        }
      }
    }

    val resultStack = inv.getStackInSlot(0)

    if (!resultStack.isEmpty) {
      val minisruft = Minecraft.getInstance()
      val scale: Float = if (resultStack.getItem.isInstanceOf[BlockItem]) 2F else 1.75F

      for (i <- 1 until 36) {
        val stacks = inv.getStackInSlot(i)
        if (stacks.isEmpty) {
          poseStack.pushPose()
          poseStack.translate(7, 4.8, 7)
          poseStack.scale(scale, scale, scale)
          minisruft.getItemRenderer.renderStatic(resultStack, ItemCameraTransforms.TransformType.GROUND, light, otherLight, poseStack, buf)
          poseStack.popPose()
        } else {
          poseStack.pushPose()
          poseStack.translate(7, 4, 7)
          poseStack.scale(scale, scale, scale)
          minisruft.getItemRenderer.renderStatic(resultStack, ItemCameraTransforms.TransformType.GROUND, light, otherLight, poseStack, buf)
          poseStack.popPose()
        }
      }
    }
    super.render(blockEntity, partialTick, poseStack, buf, light, otherLight)
  }

  // FUCKED 36 SLOTS
  private def renderItem(poseStack: MatrixStack, stack: ItemStack, x: Double, z: Double, buf: IRenderTypeBuffer, light: Int, otherLight: Int): Unit = {
    val minisruft = Minecraft.getInstance()
    poseStack.pushPose()
    poseStack.translate(x, 4.8, z)
    val scale: Float = if (stack.getItem.isInstanceOf[BlockItem]) 1F else 0.75F
    poseStack.scale(scale, scale, scale)
    minisruft.getItemRenderer.renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, light, otherLight, poseStack, buf)
    poseStack.popPose()
  }
}
// Animations is not available
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