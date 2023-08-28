package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.api.MojTextBuilder
import com.algorithmlx.inscribers.menu.InscriberContainerMenu
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.TranslationTextComponent

class InscriberMenuScreen(
  inscriber: InscriberContainerMenu, inventory: PlayerInventory
) extends ContainerScreen[InscriberContainerMenu](
  inscriber,
  inventory,
  MojTextBuilder.screen("inscriber")
) {
  override def renderBg(p_230450_1_ : MatrixStack, p_230450_2_ : Float, p_230450_3_ : Int, p_230450_4_ : Int): Unit = {

  }
}
