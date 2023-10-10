package com.algorithmlx.inscribers.api.client

import com.algorithmlx.inscribers.api.drawString
import com.algorithmlx.inscribers.api.mouseAtPosition
import com.algorithmlx.inscribers.api.texture
import com.algorithmlx.inscribers.reloc
import com.mojang.blaze3d.matrix.MatrixStack
import io.netty.util.internal.UnstableApi
import net.minecraft.client.gui.widget.Widget
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.energy.IEnergyStorage
import org.jetbrains.annotations.ApiStatus

@UnstableApi
@ApiStatus.Experimental
class EnergyWidget(x: Int, y: Int, private val storage: IEnergyStorage):
    Widget(x, y, 14, 78, StringTextComponent("Energy Bar")) {
    private val widgetTexture = reloc("textures/gui/widget/energy.png")

    override fun render(pPoseStack: MatrixStack, pMouseX: Int, pMouseY: Int, pPartialTicks: Float) {
        val offset = this.getBarOffset()

        pPoseStack.texture(this, this.widgetTexture, this.x, this.y, 0, 0)
        pPoseStack.texture(this, this.widgetTexture, this.x, this.y + this.height - offset, 14, this.height - offset, this.width, this.height)

        if (mouseAtPosition(pMouseX, pMouseY, pMouseX, pMouseY, pMouseX, pMouseY))
            pPoseStack.drawString("${this.storage.energyStored} / ${this.storage.maxEnergyStored} FE", pMouseX, pMouseY)
    }

    private fun getBarOffset(): Int {
        val i = this.storage.energyStored
        val j = this.storage.maxEnergyStored
        return if (i != 0 && j != 0) (i * (this.height / j).toLong()).toInt() else 0
    }
}