package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.Constant
import com.algorithmlx.inscribers.api.ContainerBlockEntity
import com.algorithmlx.inscribers.api.handler.{SidedItemHandlerModifiable, StackHandler}
import com.algorithmlx.inscribers.energy.InscribersEnergyStorage
import com.algorithmlx.inscribers.init.InscribersConfig
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, INamedContainerProvider}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.{ITickableTileEntity, TileEntity, TileEntityType}
import net.minecraft.util.Direction
import net.minecraft.util.Direction._
import net.minecraft.util.text.{ITextComponent, TranslationTextComponent}
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}

import scala.annotation.unused

class InscriberBlockEntity(`type`: TileEntityType[TileEntity]) extends ContainerBlockEntity(`type`) with ITickableTileEntity with INamedContainerProvider {
  // Finales
  private val energy: InscribersEnergyStorage = new InscribersEnergyStorage(InscribersConfig.INSCRIBER_CAPACITY.get(), () => {})
  private val inventory = new StackHandler(33, () => this.changeX())

  private val energyLazy: LazyOptional[InscribersEnergyStorage] = LazyOptional.of(()=> this.getEnergy)
  private val inventoryCap: Array[LazyOptional[IItemHandlerModifiable]] = SidedItemHandlerModifiable.create(
    this.inventory,
    Array[Direction](DOWN, UP, NORTH, SOUTH, WEST, EAST),
    this.canInsertFromSide,
    null
  )
  // Changeable
  @unchecked @unused // Temporarily disabled for realisation
  private val exitsSides: Array[Int] = Array[Int](0)
  private var exitSide: Int = 0 // | 0 -> Down | 1 -> Up | 2 -> North | 3 -> South | 4 -> West | 5 -> East |
  private var progress: Int = _
  private var recipe: InscriberRecipe = _

  override def getInv(): StackHandler = this.inventory

  override def tick(): Unit = {
    val level = this.getLevel
    if (level == null || level.isClientSide) return

    if (this.recipe == null || !this.recipe.matches(this.inventory.toContainer(), level)) {

    }
  }

  override def getDisplayName: ITextComponent = new TranslationTextComponent(s"menu.${Constant.ModId}.inscriber")

  override def createMenu(p_createMenu_1_ : Int, p_createMenu_2_ : PlayerInventory, p_createMenu_3_ : PlayerEntity): Container = ???

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityEnergy.ENERGY) return energyLazy.cast()

    if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (this.side == 0 && side == DOWN) return this.inventoryCap(this.side).cast()
      else if (this.side == 1 && side == UP) return this.inventoryCap(this.side).cast()
      else if (this.side == 2 && side == NORTH) return this.inventoryCap(this.side).cast()
      else if (this.side == 3 && side == SOUTH) return this.inventoryCap(this.side).cast()
      else if (this.side == 4 && side == WEST) return this.inventoryCap(this.side).cast()
      else if (this.side == 5 && side == EAST) return this.inventoryCap(this.side).cast()
    }

    super.getCapability(cap, side)
  }

  def getEnergy: InscribersEnergyStorage = {
    this.energy
  }

  def canInsertFromSide(slot: Int, stack: ItemStack, direction: Direction): Boolean = {
    false
  }

  def side: Int = {
    this.exitSide
  }

  def side(value: Int): Unit = {
    this.exitSide = value
  }
}
