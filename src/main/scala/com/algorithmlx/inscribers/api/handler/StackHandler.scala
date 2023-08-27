package com.algorithmlx.inscribers.api.handler

import net.minecraft.inventory.{IInventory, Inventory}
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemStackHandler
import org.apache.commons.lang3.ArrayUtils

import java.util
import scala.jdk.FunctionWrappers.AsJavaBiFunction

// Author: BlakeBr0 (https://github.com/BlakeBr0)
// Translate to Scala: AlgorithmLX
class StackHandler(size: Int, changeX: Runnable) extends ItemStackHandler(size) {
  private var slotSize: util.Map[Int, Int] = new util.HashMap()
  private var validator: AsJavaBiFunction[Int, ItemStack, Boolean] = _
  private var maxStack = 64
  private var outputs: Array[Int] = _

  def this(size: Int) = {
    this(size, null)
  }

  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = {
    if (this.outputs != null && ArrayUtils.contains(this.outputs, slot)) return stack

    super.insertItem(slot, stack, simulate)
  }

  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = {
    if (this.outputs != null && !ArrayUtils.contains(this.outputs, slot)) return ItemStack.EMPTY
    super.extractItem(slot, amount, simulate)
  }

  override def getSlotLimit(slot: Int): Int = if(this.slotSize.containsKey(slot)) this.slotSize.get(slot) else this.maxStack

  override def isItemValid(slot: Int, stack: ItemStack): Boolean = this.validator == null || this.validator.apply(slot, stack)

  override def onContentsChanged(slot: Int): Unit = if (this.changeX != null) this.changeX.run()

  def insert(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = super.insertItem(slot, stack, simulate)

  def extract(slot: Int, amount: Int, simulate: Boolean): ItemStack = super.extractItem(slot, amount, simulate)

  def getStackList(): NonNullList[ItemStack] = this.stacks

  def getOutputs(): Array[Int] = this.outputs

  def setDefaultSlotLimit(size: Int): Unit = this.maxStack = size

  def addSlotLimit(slot: Int, size: Int): Unit = this.slotSize.put(slot, size)

  def setValidator(validator: AsJavaBiFunction[Int, ItemStack, Boolean]) = this.validator = validator

  def setOutputSlots(slots: Array[Int]): Unit = {
    this.outputs = slots
  }

  def toContainer(): IInventory = {
    JStackHelper.invHelper(this.stacks.toArray(new Array[ItemStack](0)))
  }

  def copy(): StackHandler = {
    val copied = new StackHandler(this.getSlots, this.changeX)
    copied.setDefaultSlotLimit(this.maxStack)
    copied.setValidator(this.validator)
    copied.setOutputSlots(this.outputs)

    this.slotSize.forEach(copied.addSlotLimit)

    var i = 0
    while (i < this.getSlots) {
      val stack = this.getStackInSlot(i)
      copied.setStackInSlot(i, stack.copy)
      i += 1
    }

    copied
  }
}

