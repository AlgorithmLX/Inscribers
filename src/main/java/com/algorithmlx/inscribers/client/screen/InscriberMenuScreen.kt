package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.api.*
import com.algorithmlx.inscribers.api.client.ContainerScreenAPI
import com.algorithmlx.inscribers.block.entity.InscriberBlockEntity
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.reloc
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.IReorderingProcessor
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent

class InscriberMenuScreen(
    pMenu: InscriberContainerMenu,
    pContainer: PlayerInventory,
    pComponent: ITextComponent
) : ContainerScreenAPI<InscriberContainerMenu>(
    pMenu,
    pContainer,
    pComponent,
    reloc("textures/gui/inscriber.png"),
    176,
    280,
    328,
    328
) {
    var blockEntity: InscriberBlockEntity? = null

    override fun init() {
        super.init()

        this.blockEntity = this.getOrNullBlockEntity()
    }

    override fun renderBg(pPoseStack: MatrixStack, pPartialTicks: Float, pX: Int, pY: Int) {
        super.renderBg(pPoseStack, pPartialTicks, pX, pY)

        val x = this.guiLeft
        val y = this.guiTop

        val energy = this.getEnergyScaled(89)
        val progress = this.getProgressScaled(24)

        pPoseStack.texture(reloc("textures/gui/widget/energy.png"), x + 9, y + 26, 0F, 0F, 14, 89, 28, 89)
        blit(pPoseStack, x + 9, y + 26 - energy, 14F, 0F, 14, energy + 1, 28, 89)

        if (this.getProgress() > 0)
            pPoseStack.texture(this, this.texture, x + 79, y + 128 - progress, 230, 0, 16, progress + 1)
    }

    override fun renderLabels(pPoseStack: MatrixStack, pX: Int, pY: Int) {
//        val title = this.getTitle()
//        this.font.draw(pPoseStack, title, 7f, 7f, 4210752)
        val container = this.inventory.displayName
        this.font.draw(pPoseStack, container, 7f, this.imageHeight - 101f, 4210752)
    }

    override fun renderTooltip(pPoseStack: MatrixStack, pX: Int, pY: Int) {
        val x = this.guiLeft
        val y = this.guiTop

        super.renderTooltip(pPoseStack, pX, pY)

        if (mouseAtPosition(x + 9, y + 26, x + 22, y + 114, pX, pY)) {
            val message = translate("gui", "energy", this.getEnergyStored(), this.getMaxEnergy())
            this.renderTooltip(pPoseStack, message, pX, pY)
        } else if (mouseAtPosition(x + 79, y + 128, x + 94, y + 150, pX, pY)) {
            val msg = stringText("§e${this.getProgress()}§7/§e${this.getOperationTime()}§7")
            val message =
                if (this.getProgress() > 0 && this.getOperationTime() > 0) translate("gui", "progress", msg)
                else translate("gui", "progress", translate("gui", "progress.no"))
            val energy = this.getOperationEnergy() * this.getOperationTime()
            val processConsuming = translate("gui", "energy.consumingPerTick", energy)
            this.renderComponentTooltip(pPoseStack, listOf(message, processConsuming), pX, pY)
        }
    }

    private fun getOrNullBlockEntity(): InscriberBlockEntity? {
        val level = this.getMinecraft().level

        if (level != null) {
            val blockEntity = level.getBlockEntity(this.getMenu().pos)

            if (blockEntity is InscriberBlockEntity)
                return blockEntity
        }

        return null
    }

    fun getOperationEnergy(): Int = if (this.blockEntity != null) this.blockEntity!!.getOperationEnergy() else 0

    fun getProgress(): Int = if (this.blockEntity != null) this.blockEntity!!.progress else 0

    fun getOperationTime(): Int = if (this.blockEntity != null) this.blockEntity!!.getTime() else 0

    fun getProgressScaled(pixels: Int): Int {
        val progress = this.getProgress()
        val time = this.getOperationTime()
        return if (progress != 0 && time != 0) progress * pixels / time else 0
    }

    fun getEnergyScaled(pixels: Int): Int {
        val stored = this.getEnergyStored()
        val max = this.getMaxEnergy()
        return if (stored != 0 && max != 0) stored * pixels / max else 0
    }

    fun getEnergyStored(): Int = if (blockEntity != null) blockEntity!!.getEnergyStorage().energyStored else 0

    fun getMaxEnergy(): Int = if (blockEntity != null) blockEntity!!.getEnergyStorage().maxEnergyStored else 0
}