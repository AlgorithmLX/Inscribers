package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.Constant
import com.algorithmlx.inscribers.api.ContainerBlockEntity
import com.algorithmlx.inscribers.api.handler.{SidedItemHandlerModifiable, StackHandler}
import com.algorithmlx.inscribers.energy.InscribersEnergyStorage
import com.algorithmlx.inscribers.init.{InscribersConfig, InscribersRecipeTypes}
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, INamedContainerProvider}
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
    null,
    this.canExtractBySlot
  )
  // Changeable
  @unchecked @unused @deprecated // Temporarily disabled for realisation
  private val exitsSides: Array[Int] = Array[Int](0)
  private var exitSide: Int = 0 // | 0 -> Down | 1 -> Up | 2 -> North | 3 -> South | 4 -> West | 5 -> East |
  private var progress: Int = _
  private var recipe: InscriberRecipe = _

  override def getInv(): StackHandler = this.inventory

  override def tick(): Unit = {
    val level = this.getLevel
    if (level == null || level.isClientSide) return

    if (this.recipe == null || !this.recipe.matches(inventory)) {
      val locRecipe = level.getRecipeManager.getRecipeFor(InscribersRecipeTypes.inscriberRecipe, this.inventory.toContainer, level)
        .orElse(null)
      this.recipe = locRecipe match {
        case recipe0: InscriberRecipe => recipe0
        case _ => null
      }
    }

    if (this.recipe != null) {
      val needsEnergy = this.recipe.getEnergyCount // Needs energy per tick
      val resultTime = this.recipe.getTime
      if (this.energy.getEnergyStored >= needsEnergy) {
        this.progress += 1
        this.energy.extractEnergy(needsEnergy, simulate = false)

        if (this.progress >= resultTime) {
          var i = 0
          val finalSlot = 32
          while (i < finalSlot) {
            this.inventory.extract(i, 1, simulate = false)
            i += 1
          }
          this.inventory.setStackInSlot(32, this.recipe.result(this.inventory))
          this.progress = 0
          this.changeX()
        }
      }
    }
  }

  override def getDisplayName: ITextComponent = new TranslationTextComponent(s"menu.${Constant.ModId}.inscriber")

  override def createMenu(windowId : Int, inventory : PlayerInventory, player : PlayerEntity): Container = ???

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityEnergy.ENERGY) return energyLazy.cast()

    if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (this.getSide == 0 && side == DOWN) return this.inventoryCap(this.getSide).cast()
      else if (this.getSide == 1 && side == UP) return this.inventoryCap(this.getSide).cast()
      else if (this.getSide == 2 && side == NORTH) return this.inventoryCap(this.getSide).cast()
      else if (this.getSide == 3 && side == SOUTH) return this.inventoryCap(this.getSide).cast()
      else if (this.getSide == 4 && side == WEST) return this.inventoryCap(this.getSide).cast()
      else if (this.getSide == 5 && side == EAST) return this.inventoryCap(this.getSide).cast()
    }

    super.getCapability(cap, side)
  }

  def getEnergy: InscribersEnergyStorage = {
    this.energy
  }

  def canExtractBySlot(slot: Int, direction: Direction): Boolean = {
    if (direction == null)
      return true
    if (slot == 32) {
      if (this.getSide == 0 && direction == DOWN) return true
      else if (this.getSide == 1 && direction == UP) return true
      else if (this.getSide == 2 && direction == NORTH) return true
      else if (this.getSide == 3 && direction == SOUTH) return true
      else if (this.getSide == 4 && direction == WEST) return true
      else if (this.getSide == 5 && direction == EAST) return true
    }
    false
  }

  def getSide: Int = {
    this.exitSide
  }

  def setSide(value: Int): Unit = {
    this.exitSide = value
  }

  def getProgress: Int = {
    this.progress
  }
}
