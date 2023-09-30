package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.api.client.ContainerScreenAPI
import com.algorithmlx.inscribers.api.client.EnergyWidget
import com.algorithmlx.inscribers.block.entity.InscriberBlockEntity
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.reloc
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

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
    private var blockEntity: InscriberBlockEntity? = null

    override fun init() {
        super.init()

        this.blockEntity = this.getBlockEntity()

        if (this.blockEntity != null) {
            this.addWidget(EnergyWidget(11, 31, this.blockEntity!!.energy))
        }
    }

    override fun renderLabels(pPoseStack: MatrixStack, pX: Int, pY: Int) {
//        val title = this.getTitle()
//        this.font.draw(pPoseStack, title, 7f, 7f, 4210752)
        val container = this.inventory.displayName
        this.font.draw(pPoseStack, container, 7f, this.imageHeight - 88f, 4210752)
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

    fun getProgress(): Int = if (this.blockEntity != null) this.blockEntity!!.progress else 0

    fun getOperationTime(): Int = if (this.blockEntity != null) this.blockEntity!!.getTime() else 0

    fun getProgressScale(pixels: Int): Int {
        val progress = this.getProgress()
        val time = this.getOperationTime()
        return if (progress != 0 && time != 0) ((progress * pixels).toLong()).toInt() else 0
    }
}