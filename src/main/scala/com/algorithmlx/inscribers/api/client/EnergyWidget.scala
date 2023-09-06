package com.algorithmlx.inscribers.api.client

import com.algorithmlx.inscribers.Constant.reloc
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.Widget
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.energy.IEnergyStorage

class EnergyWidget(x: Int, y: Int, private val storage: IEnergyStorage) extends Widget(x, y, 14, 78, new StringTextComponent("Energy Bar")) {
  private val widgetTexture = reloc("textures/gui/widget/energy.png")

  override def render(poseStack : MatrixStack, mouseX : Int, mouseY : Int, partialTick : Float): Unit = {
    val offset = this.getBarOffset

    Minecraft.getInstance().getTextureManager.bind(this.widgetTexture)
    blit(poseStack, this.x, this.y, 0, 0, this.width, this.height)
    Minecraft.getInstance().getTextureManager.bind(this.widgetTexture)
    blit(poseStack, this.x, this.y + this.height - offset, 14, this.height - offset, this.width, offset + 1)

    if (isCursorAtButton(mouseX, mouseY)) {
      val font = Minecraft.getInstance().font
      val text = new StringTextComponent(s"${this.storage.getEnergyStored} / ${this.storage.getMaxEnergyStored} FE")
      font.draw(poseStack, text, mouseX, mouseY, 0xFFFFFF)
    }
  }

  private def getBarOffset: Int = {
    val i = this.storage.getEnergyStored
    val j = this.storage.getMaxEnergyStored
    if (i != 0 && j != 0) (i * (this.height / j).longValue).toInt else 0
  }

  def isCursorAtButton(cursorX: Int, cursorY: Int): Boolean = cursorX >= x && cursorY >= y && cursorX <= x + width && cursorY <= y + height
}
