package com.algorithmlx.inscribers.menu

import com.algorithmlx.inscribers.api.handler.StackHandlerSlot
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.container.InscriberCraftingContainer
import com.algorithmlx.inscribers.container.slot.InscriberResultSlot
import com.algorithmlx.inscribers.init.registry.{InscribersRecipeTypes, Register}
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.inventory.{CraftResultInventory, IInventory}
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{IIntArray, IntArray}
import net.minecraft.world.World
import net.minecraftforge.items.wrapper.InvWrapper
import net.minecraftforge.items.{IItemHandler, SlotItemHandler}

//noinspection DuplicatedCode
class InscriberContainerMenu(
  `type`: ContainerType[_],
  windowId: Int,
  inventory: PlayerInventory,
  private val usedByPlayer: PlayerEntity => Boolean,
  private val data: IIntArray,
  private val pos: BlockPos
) extends Container(`type`, windowId) {
  private val result: IInventory = new CraftResultInventory()
  private val craftInventory: IInventory = new InscriberCraftingContainer(this, new InscriberBlockEntity(Register.INSCRIBER_BLOCK_ENTITY.get()).getInv(), 6)
  private val level: World = inventory.player.level

  def this(`type`: ContainerType[_], id: Int, inventory: PlayerInventory, pack: PacketBuffer) = {
    this(
      `type`,
      id,
      inventory,
      _ => false,
      new IntArray(7),
      pack.readBlockPos()
    )
  }

  this.addSlot(new InscriberResultSlot(this, craftInventory, this.result, 0, 79, 163))

  for (index <- 1 until 36) {
    for (i <- 0 until 6) {
      for (j <- 0 until 6) {
        this.addSlot(new StackHandlerSlot(new InscriberBlockEntity(Register.INSCRIBER_BLOCK_ENTITY.get()).getInv(), index, 34 + i * 18, 17 + j * 18))
      }
    }
  }

  this.slotsChanged(craftInventory)
  this.addDataSlots(data)

  this.addPlayerInventory(inventory, 8, 198)

  override def slotsChanged(container : IInventory): Unit = {
    val recipe = this.level.getRecipeManager.getRecipeFor(InscribersRecipeTypes.inscriberRecipe, container, this.level)
    if (recipe.isPresent) {
      val result = recipe.get().assemble(container)
      this.result.setItem(0, result)
    } else this.result.setItem(0, ItemStack.EMPTY)

    super.slotsChanged(container)
  }

  override def stillValid(player : PlayerEntity): Boolean = this.usedByPlayer.apply(player)

  override def quickMoveStack(player : PlayerEntity, index : Int): ItemStack = ItemStack.EMPTY

  def getPos: BlockPos = this.pos

  def addSlotRange(handler: IItemHandler, index: Int, x: Int, y: Int, amount: Int, dx: Int): Int = {
    var index0 = index
    var x0 = x

    for (_ <- 0 until amount) {
      this.addSlot(new SlotItemHandler(handler, index0, x0, y))
      x0 += dx
      index0 += 1
    }

    index0
  }

  def addSlotBox(handler: IItemHandler, index: Int, x: Int, y: Int, horAmount: Int, dx: Int, verAmount: Int, dy: Int): Unit = {
    var index0 = index
    var y0 = y

    for (_ <- 0 until verAmount) {
      index0 = this.addSlotRange(handler, index0, x, y0, horAmount, dx)
      y0 += dy
    }
  }

  def addPlayerInventory(inventory: PlayerInventory, leftColX: Int, topRowY: Int): Unit = {
    var topRowY0 = topRowY
    val playerInv = new InvWrapper(inventory)
    addSlotBox(playerInv, 9, leftColX, topRowY0, 9, 18, 3, 18)
    topRowY0 += 58
    addSlotRange(playerInv, 0, leftColX, topRowY0, 9, 18)
  }

}
