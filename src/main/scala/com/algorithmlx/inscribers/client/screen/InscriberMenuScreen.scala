package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.menu.InscriberContainerMenu
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class InscriberMenuScreen(
  inscriber: InscriberContainerMenu,
  inventory: PlayerInventory,
  iTextComponent: ITextComponent
) extends ContainerScreen[InscriberContainerMenu](
  inscriber,
  inventory,
  iTextComponent
) {
  override def init(): Unit = {
    super.init()
  }

  override def renderBg(pose : MatrixStack, partialTick : Float, p_230450_3_ : Int, p_230450_4_ : Int): Unit = {

  }
}
