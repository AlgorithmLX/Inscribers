package com.algorithmlx.inscribers.api.client

import com.algorithmlx.inscribers.api.container.AbstractContainerMenu
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent

open class ContainerScreenAPI<T: AbstractContainerMenu>(
    pMenu: T,
    pPlayerInventory: PlayerInventory,
    pTitle: ITextComponent,
    protected val texture: ResourceLocation,
    width: Int,
    height: Int,
    protected val textureWidth: Int = 256,
    protected val textureHeight: Int = 256
) : ContainerScreen<T>(
    pMenu,
    pPlayerInventory,
    pTitle
) {
    init {
        this.imageWidth = width
        this.imageHeight = height
    }

    override fun render(pPoseStack: MatrixStack, pMouseX: Int, pMouseY: Int, pPartialTicks: Float) {
        this.renderBackground(pPoseStack)
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTicks)
        this.renderTooltip(pPoseStack, pMouseX, pMouseY)
    }

    override fun renderBg(pPoseStack: MatrixStack, pPartialTicks: Float, pX: Int, pY: Int) {
        this.getMinecraft().getTextureManager().bind(this.texture)
        val x = this.guiLeft
        val y = this.guiTop
        AbstractGui.blit(pPoseStack, x, y, 0f, 0f, this.imageWidth, this.imageHeight, this.textureWidth, this.textureHeight)
    }
}