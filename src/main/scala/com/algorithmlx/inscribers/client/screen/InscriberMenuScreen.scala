package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.api.client.EnergyWidget
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.client.widget.InscriberConfigureButton
import com.algorithmlx.inscribers.menu.InscriberContainerMenu
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class InscriberMenuScreen(
  inscriber: InscriberContainerMenu,
  inventory: PlayerInventory,
  iTextComponent: ITextComponent
) extends ContainerScreen[InscriberContainerMenu](
  inscriber,
  inventory,
  iTextComponent
) {
  private val texture = reloc("textures/gui/inscriber.png")
  private var blockEntity: InscriberBlockEntity = _
  /*
  -------------------------------------------------------------------------
  | 0 -> Down | 1 -> Up | 2 -> North | 3 -> South | 4 -> West | 5 -> East |
  _________________________________________________________________________
  */
  override def init(): Unit = {
    super.init()
    this.addButton(new InscriberConfigureButton(196, 54, 0))
    this.addButton(new InscriberConfigureButton(196, 34, 1))
    this.addButton(new InscriberConfigureButton(196, 44, 2))
    this.addButton(new InscriberConfigureButton(206, 54, 3))
    this.addButton(new InscriberConfigureButton(206, 44, 4))
    this.addButton(new InscriberConfigureButton(186, 44, 5))

    this.blockEntity = this.getBlockEntity

    if (this.blockEntity != null) this.addWidget(new EnergyWidget(11, 31, this.blockEntity.getEnergy))
  }

  override def render(poseStack : MatrixStack, mouseX : Int, mouseY : Int, partialTick : Float): Unit = {
    this.renderBackground(poseStack)
    super.render(poseStack, mouseX, mouseY, partialTick)
    this.renderTooltip(poseStack, mouseX, mouseY)
  }

  override def renderBg(pose : MatrixStack, partialTick : Float, mouseX : Int, mouseY : Int): Unit = {
    this.getMinecraft.getTextureManager.bind(this.texture)
    this.getMinecraft.getTextureManager.bind(this.texture)
    this.getMinecraft.getTextureManager.bind(this.texture)
    this.getMinecraft.getTextureManager.bind(this.texture)

    val x = this.getGuiLeft
    val y = this.getGuiTop

    AbstractGui.blit(pose, x + 176, y + 21, 277, 0, 51, 55, 328, 328)
    AbstractGui.blit(pose, x + 186, y + 134, 300, 77, 28, 28, 328, 328)
    AbstractGui.blit(pose, x + 210, y + 25, 315, 107, 13, 13, 328, 328)
    AbstractGui.blit(pose, x, y, 0, 0, 176, 280, 328, 328)

    if (this.getProgress > 0) {
      val progressScale = this.getProgressScale(24)
      this.blit(pose, x + 78, y + 129, 299, 0, progressScale + 1, 17)
    }
  }

  private def getBlockEntity: InscriberBlockEntity = {
    val level = this.getMinecraft.level

    if (level != null) {
      val blockEntity = level.getBlockEntity(this.getMenu.getPos)

      if (blockEntity.isInstanceOf[InscriberBlockEntity]) return blockEntity.asInstanceOf
    }

    null
  }

  def getProgress: Int = if (this.blockEntity == null) 0 else this.blockEntity.getProgress

  def getOperationTime: Int = if (this.blockEntity == null) 0 else this.blockEntity.getTime

  def getProgressScale(pix: Int): Int = {
    val progress = this.getProgress
    val time = this.getOperationTime
    if (progress != 0 && time != 0) ((progress * pix).longValue / time).intValue else 0
  }
}
