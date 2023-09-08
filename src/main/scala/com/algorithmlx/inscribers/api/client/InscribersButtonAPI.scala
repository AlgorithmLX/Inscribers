package com.algorithmlx.inscribers.api.client

import com.algorithmlx.inscribers.api.client.InscribersButtonAPI.getActivated
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.client.gui.widget.button.Button.{IPressable, ITooltip}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{ITextComponent, StringTextComponent}

class InscribersButtonAPI private(
  private val buttonX: Int,
  private val buttonY: Int,
  private val buttonWidth: Int,
  private val buttonHeight: Int,
  private val id: Int,
  private val text: ITextComponent,
  onPress: IPressable,
  tooltip: ITooltip,
  private val reloc: ResourceLocation,
  private val stack: ItemStack,
  private val stackedTexture: Boolean,
  private val enableDoubleButton: Boolean
) extends Button(buttonX, buttonY, buttonWidth, buttonHeight, text, onPress, tooltip) {

  def this(x: Int, y: Int, width: Int, height: Int, id: Int, text: ITextComponent, onPress: IPressable, tooltip: ITooltip, reloc: ResourceLocation, enableDoubleButton: Boolean) = {
    this(x, y, width, height, id, text, onPress, tooltip, reloc, ItemStack.EMPTY, false, enableDoubleButton)
  }

  def this(x: Int, y: Int, width: Int, height: Int, id: Int, text: ITextComponent, onPress: IPressable, reloc: ResourceLocation) = {
    this(x, y, width, height, id, text, onPress, (_: Button, _: MatrixStack, _: Int, _: Int) => {}, reloc, true)
  }

  def this(x: Int, y: Int, width: Int, height: Int, id: Int, text: ITextComponent, onPress: IPressable, tooltip: ITooltip, stack: ItemStack, enableDoubleButton: Boolean) = {
    this(x, y, width, height, id, text, onPress, tooltip, null, stack, true, enableDoubleButton)
  }

  def this(x: Int, y: Int, width: Int, height: Int, id: Int, text: ITextComponent, onPress: IPressable, stack: ItemStack, enableDoubleButton: Boolean) = {
    this(x, y, width, height, id, text, onPress, (_: Button, _: MatrixStack, _: Int, _: Int) => {}, stack, enableDoubleButton)
  }

  override def render(poseStack : MatrixStack, mouseX : Int, mouseY : Int, partialTick : Float): Unit = {
    val mc = Minecraft.getInstance()
    val font = mc.font

    poseStack.pushPose()
    if (this.text != null)
      font.draw(poseStack, this.text, this.buttonX * 1F, (this.buttonY + this.buttonHeight) / 4F, 0xFFFFFF)

    if (!this.stackedTexture) {
      val textureW = this.buttonWidth / 2
      val textureH = this.buttonHeight / 2
      Minecraft.getInstance().getTextureManager.bind(this.reloc)
      if (this.enableDoubleButton) {
        if (!getActivated(id)) {
          if (!this.isCursorAtButton(mouseX, mouseY)) {
            blit(poseStack, textureW, textureH, 0, 0, this.buttonWidth, this.buttonHeight)
          } else {
            blit(poseStack, textureW, textureH, 0, 8, this.buttonWidth, this.buttonHeight)
          }
        } else {
          if (!this.isCursorAtButton(mouseX, mouseY)) {
            blit(poseStack, textureW, textureH, 8, 0, this.buttonWidth, this.buttonHeight)
          } else {
            blit(poseStack, textureW, textureH, 8, 8, this.buttonWidth, this.buttonHeight)
          }
        }
      }
    } else {
      this.renderItem(poseStack, this.stack, this.buttonX, this.buttonY)
    }

    poseStack.popPose()
  }

  protected def renderItem(poseStack: MatrixStack, stack: ItemStack, x: Int, y: Int): Unit = {
    RenderSystem.pushMatrix()
    RenderSystem.multMatrix(poseStack.last().pose())
    poseStack.pushPose()
    Minecraft.getInstance().getItemRenderer.renderAndDecorateItem(stack, x, y)
    poseStack.popPose()
    RenderSystem.popMatrix()
  }

  def isCursorAtButton(cursorX: Int, cursorY: Int): Boolean = cursorX >= buttonX && cursorY >= buttonY && cursorX <= buttonX + buttonWidth&& cursorY <= buttonY + buttonHeight
}

object InscribersButtonAPI {
  private var activated = false
  private var id = 0

  def getActivated: Boolean = getActivated(getId)

  def setActivated(value: Boolean): Unit = this.setActivated(value, getId)

  def getActivated(id: Int): Boolean = this.id == id && this.activated

  def setActivated(value: Boolean, id: Int): Unit = {
    this.activated = value
    this.setId(id)
  }

  def getId: Int = this.id

  def setId(id: Int): Unit = {
    this.id = id
  }
}