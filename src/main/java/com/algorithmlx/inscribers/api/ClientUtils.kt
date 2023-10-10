package com.algorithmlx.inscribers.api

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.Widget
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

val mc: Minecraft = Minecraft.getInstance()
val font: FontRenderer = mc.font

fun MatrixStack.drawString(text: String, mouseX: Int, mouseY: Int) {
    this.drawString(text, mouseX, mouseY, 0xFFFFFF)
}

fun MatrixStack.drawString(text: String, mouseX: Float, mouseY: Float) {
    this.drawString(text, mouseX, mouseY, 0xFFFFFF)
}

fun MatrixStack.drawString(text: ITextComponent, mouseX: Int, mouseY: Int) {
    this.drawString(text, mouseX, mouseY, 0xFFFFFF)
}

fun MatrixStack.drawString(text: ITextComponent, mouseX: Float, mouseY: Float) {
    this.drawString(text, mouseX, mouseY, 0xFFFFFF)
}

fun MatrixStack.drawString(text: String, mouseX: Int, mouseY: Int, color: Int) {
    this.drawString(text, mouseX.toFloat(), mouseY.toFloat(), color)
}

fun MatrixStack.drawString(text: String, mouseX: Float, mouseY: Float, color: Int) {
    this.drawString(StringTextComponent(text), mouseX, mouseY, color)
}

fun MatrixStack.drawString(text: ITextComponent, mouseX: Int, mouseY: Int, color: Int) {
    this.drawString(text, mouseX.toFloat(), mouseY.toFloat(), color)
}

fun MatrixStack.drawString(text: ITextComponent, mouseX: Float, mouseY: Float, color: Int) {
    font.draw(this, text, mouseX, mouseY, color)
}

fun mouseAtPosition(x: Int, y: Int, x1: Int, y1: Int, mouseX: Int, mouseY: Int): Boolean =
    mouseX >= x && mouseY >= y && mouseX <= x1 && mouseY <= y1

fun MatrixStack.texture(widget: Widget, id: ResourceLocation, x: Int, y: Int, uOffset: Int, vOffset: Int) {
    this.texture(widget, id, x, y, uOffset, vOffset, widget.width, widget.height)
}

fun MatrixStack.texture(screen: AbstractGui, id: ResourceLocation, x: Int, y: Int, uOffset: Int, vOffset: Int, width: Int, height: Int) {
    mc.textureManager.bind(id)
    screen.blit(this, x, y, uOffset, vOffset, width, height)
}

fun MatrixStack.texture(id: ResourceLocation, x: Int, y: Int, uOffset: Float, vOffset: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int) {
    mc.textureManager.bind(id)
    AbstractGui.blit(this, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight)
}

fun MatrixStack.renderItem(stack: ItemStack, x: Int, y: Int) {
    RenderSystem.pushMatrix()
    RenderSystem.multMatrix(this.last().pose())
    this.pushPose()
    mc.itemRenderer.renderAndDecorateItem(stack, x, y)
    this.popPose()
    RenderSystem.popMatrix()
}

fun MatrixStack.renderItem(stack: ItemStack, x: Double, y: Double, z: Double, light: Int, lightOther: Int, buf: IRenderTypeBuffer) {
    val scale: Float = if (stack.item is BlockItem) 1F else 0.75F
    this.renderItem(stack, scale, x, y, z, light, lightOther, buf)
}

fun MatrixStack.renderItem(stack: ItemStack, scale: Float, x: Double, y: Double, z: Double, light: Int, lightOther: Int, buf: IRenderTypeBuffer) {
    this.pushPose()
    this.translate(x, y, z)
    this.scale(scale, scale, scale)
    mc.itemRenderer.renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, light, lightOther, this, buf)
    this.popPose()
}
