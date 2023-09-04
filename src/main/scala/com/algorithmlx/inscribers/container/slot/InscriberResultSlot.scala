package com.algorithmlx.inscribers.container.slot

import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.{Container, Slot}
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.common.ForgeHooks

class InscriberResultSlot(
  private val containerMenu: Container,
  private val containerGlob: IInventory,
  container: IInventory,
  index: Int,
  x: Int,
  y: Int
) extends Slot(container, index, x, y) {
  override def mayPlace(p_75214_1_ : ItemStack): Boolean = false

  override def onTake(player : PlayerEntity, stack : ItemStack): ItemStack = {
    ForgeHooks.setCraftingPlayer(player)

    val remain: NonNullList[ItemStack] = player.level.getRecipeManager.getRemainingItemsFor(InscribersRecipeTypes.inscriberRecipe, this.containerGlob, player.level)

    var i: Int = 0

    while (i < remain.size()) {
      var slotItem = this.containerGlob.getItem(i)
      val remStack = remain.get(i)

      if (!slotItem.isEmpty) {
        this.containerGlob.removeItem(i, 1)
        slotItem = this.containerGlob.getItem(i)
      }

      if (!remStack.isEmpty) {
        if (slotItem.isEmpty) this.containerGlob.setItem(i, remStack)
        else if (ItemStack.isSame(slotItem, remStack) && ItemStack.tagMatches(slotItem, remStack)) {
          remStack.grow(slotItem.getCount)
          this.containerGlob.setItem(i, remStack)
        }
        else if (!player.inventory.add(remStack)) player.drop(remStack, false)
      }

      i += 1
    }

    this.containerMenu.slotsChanged(this.containerGlob)

    stack
  }

}
