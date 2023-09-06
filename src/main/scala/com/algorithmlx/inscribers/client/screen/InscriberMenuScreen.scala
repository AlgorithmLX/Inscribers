package com.algorithmlx.inscribers.client.screen

import com.algorithmlx.inscribers.api.client.EnergyWidget
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.client.widget.InscriberConfigureButton
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
  private var blockEntity: InscriberBlockEntity = _
  /*
  -------------------------------------------------------------------------
  | 0 -> Down | 1 -> Up | 2 -> North | 3 -> South | 4 -> West | 5 -> East |
  _________________________________________________________________________
  */
  override def init(): Unit = {
    super.init()
    this.addButton(new InscriberConfigureButton(196, 54, 0))
    this.addButton(new InscriberConfigureButton(196, 34, 1))
    this.addButton(new InscriberConfigureButton(196, 44, 2))
    this.addButton(new InscriberConfigureButton(206, 54, 3))
    this.addButton(new InscriberConfigureButton(206, 44, 4))
    this.addButton(new InscriberConfigureButton(186, 44, 5))

    this.blockEntity = this.getBlockEntity

    if (this.blockEntity != null)
      this.addWidget(new EnergyWidget(11, 31, this.blockEntity.getEnergy))
  }

  override def renderBg(pose : MatrixStack, partialTick : Float, p_230450_3_ : Int, p_230450_4_ : Int): Unit = {

  }

  private def getBlockEntity: InscriberBlockEntity = {
    val level = this.getMinecraft.level

    if (level != null) {
      val blockEntity = level.getBlockEntity(this.getMenu.getPos)

      if (blockEntity.isInstanceOf[InscriberBlockEntity]) return blockEntity.asInstanceOf
    }

    null
  }
}
