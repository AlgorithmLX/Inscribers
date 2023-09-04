package com.algorithmlx.inscribers.api.helper

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.util.RecipeMatcher
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

trait RecipeHelper extends IRecipe[IInventory] {
  override def assemble(inventory: IInventory): ItemStack = this.result(new InvWrapper(inventory))

  override def matches(inventory : IInventory, level : World): Boolean = this.matches(new InvWrapper(inventory))

  override def getRemainingItems(inventory: IInventory): NonNullList[ItemStack] = this.getRemainingItems(new InvWrapper(inventory))

  def result(itemHandler: IItemHandler): ItemStack

  def matches(handler: IItemHandler): Boolean = this.matches(handler, 0, handler.getSlots)

  def matches(handler: IItemHandler, startIndex: Int, finishIndex: Int): Boolean = {
    val inputs: NonNullList[ItemStack] = NonNullList.create()
    // analog on java: for (int i = 0; i < finishIndex; i++)
    // analog on kotlin: for (i in 0 until finishIndex)
    var i = startIndex

    while (i < finishIndex) {
      inputs.add(handler.getStackInSlot(i))
      i += 1
    }

    RecipeMatcher.findMatches(inputs, this.getIngredients) != null
  }

  def getRemainingItems(handler: IItemHandler): NonNullList[ItemStack] = {
    val items: NonNullList[ItemStack] = NonNullList.withSize(handler.getSlots, ItemStack.EMPTY)
    var i = 0
    while (i < items.size()) {
      val stack = handler.getStackInSlot(i)
      if (stack.hasContainerItem) items.set(i, stack.getContainerItem)

      i += 1
    }

    items
  }
}
