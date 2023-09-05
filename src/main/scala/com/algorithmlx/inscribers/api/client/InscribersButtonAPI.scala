package com.algorithmlx.inscribers.api.client

import com.algorithmlx.inscribers.Constant
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.client.gui.widget.button.Button.{IPressable, ITooltip}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{ITextComponent, StringTextComponent}

class InscribersButtonAPI private(
  private val x: Int,
  private val y: Int,
  private val width: Int,
  private val height: Int,
  private val text: ITextComponent,
  onPress: IPressable,
  tooltip: ITooltip,
  private val reloc: ResourceLocation,
  private val stack: ItemStack,
  private val stackedTexture: Boolean
) extends Button(x, y, width, height, text, onPress, tooltip) {
  private val emptyTooltip: ITooltip = (_, _, _, _) => {}
  private val emptyReLoc = new ResourceLocation("")
  private val emptyStack = ItemStack.EMPTY
  private val emptyText = new StringTextComponent("")

  private val texture = Constant.reloc("textures/gui/button/inscriber_button.png")

  def this(x: Int, y: Int, width: Int, height: Int, text: ITextComponent, onPress: IPressable, tooltip: ITooltip, reloc: ResourceLocation) = {
    this(x, y, width, height, text, onPress, tooltip, reloc, this.emptyStack, false)
  }

  def this(x: Int, y: Int, width: Int, height: Int, text: ITextComponent, onPress: IPressable, reloc: ResourceLocation) = {
    this(x, y, width, height, text, onPress, this.emptyTooltip, reloc)
  }

  def this(x: Int, y: Int, width: Int, height: Int, text: ITextComponent, onPress: IPressable, tooltip: ITooltip, stack: ItemStack) = {
    this(x, y, width, height, text, onPress, tooltip, this.emptyReLoc, stack, true)
  }

  def this(x: Int, y: Int, width: Int, height: Int, text: ITextComponent, onPress: IPressable, stack: ItemStack) = {
    this(x, y, width, height, text, onPress, this.emptyTooltip, stack)
  }

  override def render(poseStack : MatrixStack, mouseX : Int, mouseY : Int, partialTick : Float): Unit = {
    val mc = Minecraft.getInstance()
    val font = mc.font

    poseStack.pushPose()
    if (this.text != null || this.text == this.emptyText)
      font.draw(poseStack, this.text, this.x, this.y + this.height / 4F, 0xFFFFFF)

    if (!this.stackedTexture) {

//      Minecraft.getInstance().getTextureManager.bind()
    } else {
      this.renderItem(poseStack, this.stack, this.x, this.y)
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

  def isCursorAtButton(cursorX: Int, cursorY: Int): Boolean = cursorX >= x && cursorY >= y && cursorX <= x + width&& cursorY <= y + height
}
object InscribersButtonAPI {
  private var activated = false
  def setActivated(value: Boolean): Unit = {
    this.activated = value
  }
  def getActivated: Boolean = this.activated
}