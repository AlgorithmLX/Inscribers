package com.algorithmlx.inscribers.api.client

import com.algorithmlx.inscribers.api.drawString
import com.algorithmlx.inscribers.api.mouseAtPosition
import com.algorithmlx.inscribers.api.mc
import com.algorithmlx.inscribers.api.renderItem
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TextFormatting

open class InscribersButtonAPI(
    private val buttonX: Int,
    private val buttonY: Int,
    private val buttonWidth: Int,
    private val buttonHeight: Int,
    private val id: Int,
    private val text: ITextComponent = StringTextComponent(""),
    onPress: IPressable,
    tooltip: ITooltip = ITooltip { _, _, _, _ -> },
    private val texture: ResourceLocation? = null,
    private val stack: ItemStack =  ItemStack.EMPTY,
    private val stackedTexture: Boolean = false,
    private val enableDoubleButton: Boolean = true
): Button(buttonX, buttonY, buttonWidth, buttonHeight, text, onPress, tooltip) {
    override fun render(pPoseStack: MatrixStack, pMouseX: Int, pMouseY: Int, pPartialTicks: Float) {
        pPoseStack.pushPose()
        if (this.text != StringTextComponent(""))
            pPoseStack.drawString(this.text, this.buttonX * 1F, (this.buttonY + this.buttonHeight) / 4F)

        if (!this.stackedTexture && this.texture != null) {
            val textureW = this.buttonWidth / 2
            val textureH = this.buttonHeight / 2
            mc.textureManager.bind(this.texture)
            if (this.enableDoubleButton) {
                if (!getActivated(this.id)) {
                    if (mouseAtPosition(pMouseX, pMouseY, pMouseX, pMouseY, pMouseX, pMouseY))
                        blit(pPoseStack, textureW, textureH, 0, 0, this.buttonWidth, this.buttonHeight)
                    else blit(pPoseStack, textureW, textureH, 0, 8, this.buttonWidth, buttonHeight)
                } else {
                    if (!mouseAtPosition(pMouseX, pMouseY, pMouseX, pMouseY, pMouseX, pMouseY))
                        blit(pPoseStack, textureW, textureH, 8, 0, this.buttonWidth, this.buttonHeight)
                    else blit(pPoseStack, textureW, textureH, 8, 8, this.buttonWidth, this.buttonHeight)
                }
            } else {
                pPoseStack.renderItem(this.stack, this.buttonX, this.buttonY)
            }
        }
    }

    companion object {
        private var activated = false
        var id = 0

        fun getActivated(): Boolean = getActivated(id)

        fun setActivated(value: Boolean) {
            this.setActivated(value, this.id)
        }

        fun getActivated(id: Int): Boolean = this.id == id  && this.activated

        fun setActivated(value: Boolean, id: Int) {
            this.activated = value
            this.id = id
        }
    }
}