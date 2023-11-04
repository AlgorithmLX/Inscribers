package com.algorithmlx.inscribers.container.slot

import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.common.ForgeHooks

class InscriberResultSlot(
    private val containerMenu: Container,
    private val containerGlobal: IInventory,
    container: IInventory,
    index: Int,
    x: Int,
    y: Int
) : Slot(container, index, x, y) {
    override fun mayPlace(pStack: ItemStack): Boolean = false

    override fun onTake(pPlayer: PlayerEntity, pStack: ItemStack): ItemStack {
        ForgeHooks.setCraftingPlayer(pPlayer)

        val remain = pPlayer.level.recipeManager.getRemainingItemsFor(InscribersRecipeTypes.inscriberRecipe, this.containerGlobal, pPlayer.level)

        for (i in 0 until remain.size) {
            var slotItem = this.containerGlobal.getItem(i)
            val remStack = remain[i]

            if (!slotItem.isEmpty) {
                this.containerGlobal.removeItem(i, 1)
                slotItem = this.containerGlobal.getItem(i)
            }

            if (!remStack.isEmpty) {
                if (slotItem.isEmpty) this.containerGlobal.setItem(i, remStack)
                else if (ItemStack.isSame(slotItem, remStack) && ItemStack.tagMatches(slotItem, remStack)) {
                    remStack.grow(slotItem.count)
                    this.containerGlobal.setItem(i, remStack)
                }
                else if (!pPlayer.inventory.add(remStack)) pPlayer.drop(remStack, false)
            }
        }

        this.containerMenu.slotsChanged(this.containerGlobal)

        return pStack
    }
}
