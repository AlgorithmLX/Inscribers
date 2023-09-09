package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.api.client.EnergyWidget
import com.algorithmlx.inscribers.api.texture
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.client.widget.InscriberConfigureButton
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.reloc
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class InscriberMenuScreen(
    inscriber: InscriberContainerMenu,
    inventory: PlayerInventory,
    iTextComponent: ITextComponent
) : ContainerScreen<InscriberContainerMenu>(inscriber, inventory, iTextComponent) {
    private val texture = reloc("textures/gui/inscriber.png")
    private var blockEntity: InscriberBlockEntity? = null

    override fun init() {
        super.init()
        this.addButton(InscriberConfigureButton(196, 54, 0))
        this.addButton(InscriberConfigureButton(196, 34, 1))
        this.addButton(InscriberConfigureButton(196, 44, 2))
        this.addButton(InscriberConfigureButton(206, 54, 3))
        this.addButton(InscriberConfigureButton(206, 44, 4))
        this.addButton(InscriberConfigureButton(186, 44, 5))

        this.blockEntity = this.getBlockEntity()

        if (this.blockEntity != null) this.addWidget(EnergyWidget(11, 31, this.blockEntity!!.energy))
    }

    override fun render(pPoseStack: MatrixStack, pMouseX: Int, pMouseY: Int, pPartialTicks: Float) {
        this.renderBackground(pPoseStack)
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTicks)
        this.renderTooltip(pPoseStack, pMouseX, pMouseY)
    }

    override fun renderBg(pPoseStack: MatrixStack, pPartialTicks: Float, pX: Int, pY: Int) {
        val x  = this.guiLeft
        val y = this.guiTop

        pPoseStack.texture(this.texture, x + 176, y + 21, 277F, 0F, 51, 55, 328, 328)
        pPoseStack.texture(this.texture, x + 186, y + 134, 300F, 77F, 28, 28, 328, 328)
        pPoseStack.texture(this.texture, x + 210, y + 25, 315F, 107F, 13, 13, 328, 328)
        pPoseStack.texture(this.texture, x, y, 0F, 0F, 176, 280, 328, 328)

        if (this.getProgress() > 0) {
            val progressScale = this.getProgressScale(24)
            pPoseStack.texture(this, this.texture, x + 78, y + 129, 299, 0, progressScale + 1, 17)
        }
    }

    private fun getBlockEntity(): InscriberBlockEntity? {
        val level = this.getMinecraft().level

        if (level != null) {
            val blockEntity = level.getBlockEntity(this.getMenu().pos)

            if (blockEntity is InscriberBlockEntity)
                return blockEntity
        }

        return null
    }

    fun getProgress(): Int = if (this.blockEntity != null) this.blockEntity!!.getProgress() else 0

    fun getOperationTime(): Int = if (this.blockEntity != null) this.blockEntity!!.getTime() else 0

    fun getProgressScale(pixels: Int): Int {
        val progress = this.getProgress()
        val time = this.getOperationTime()
        return if (progress != 0 && time != 0) ((progress * pixels).toLong()).toInt() else 0
    }
}