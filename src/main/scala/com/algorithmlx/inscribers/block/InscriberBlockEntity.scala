package com.algorithmlx.inscribers.block

import com.algorithmlx.inscribers.api.block.ContainerBlockEntity
import com.algorithmlx.inscribers.api.handler.{SidedItemHandlerModifiable, StackHandler}
import com.algorithmlx.inscribers.api.helper.MojTextHelper
import com.algorithmlx.inscribers.energy.InscribersEnergyStorage
import com.algorithmlx.inscribers.init.config.InscribersConfig
import com.algorithmlx.inscribers.init.registry.{InscribersRecipeTypes, Register}
import com.algorithmlx.inscribers.network.InscribersNetwork
import com.algorithmlx.inscribers.network.packet.SDirectionPack
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import com.algorithmlx.inscribers.server.InscriberDirectionSettingsServer
import net.minecraft.block.BlockState
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, INamedContainerProvider}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.{ITickableTileEntity, TileEntity, TileEntityType}
import net.minecraft.util.Direction
import net.minecraft.util.Direction._
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}

import scala.annotation.unused

class InscriberBlockEntity(`type`: TileEntityType[TileEntity] = Register.INSCRIBER_BLOCK_ENTITY.get()) extends ContainerBlockEntity(`type`) with ITickableTileEntity with INamedContainerProvider {
  // Constants
  private val energy: InscribersEnergyStorage = new InscribersEnergyStorage(InscribersConfig.INSCRIBER_CAPACITY.get(), () => {})
  private val inventory = new StackHandler(36, () => this.changeX())
  protected var isWorking = false

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

  private var progress: Int = _
  private var recipe: InscriberRecipe = _

  override def getInv: StackHandler = this.inventory

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
      this.isWorking = true
      if (this.energy.getEnergyStored >= needsEnergy) {
        this.progress += 1
        this.energy.extractEnergy(needsEnergy, simulate = false)

        if (this.progress >= resultTime) {
          var i = 1
          val finalSlot = 36
          while (i < finalSlot) {
            this.inventory.extract(i, 1, simulate = false)
            i += 1
          }
          this.inventory.setStackInSlot(0, this.recipe.result(this.inventory))
          this.progress = 0
          this.isWorking = false
          this.changeX()
        }
      }
    }
  }

  override def save(tag: CompoundNBT): CompoundNBT = {
    super.save(tag)
    tag.putInt("InscriberProgress", this.progress)
    tag.putInt("InscriberEnergy", this.energy.getEnergyStored)
    tag.putInt("InscriberExitSide", InscriberDirectionSettingsServer.getData)
    tag.putBoolean("EnableInscriberExitSide", InscriberDirectionSettingsServer.getEnabled)
    tag
  }

  override def load(state: BlockState, tag: CompoundNBT): Unit = {
    super.load(state, tag)
    this.progress = tag.getInt("InscriberProgress")
    this.energy.setStored(tag.getInt("InscriberEnergy"))
    InscribersNetwork.sendToServer(new SDirectionPack(tag.getInt("InscriberExitSide"), tag.getBoolean("EnableInscriberExitSide")))
  }

  override def getDisplayName: ITextComponent = MojTextHelper.menu("inscriber")

  override def createMenu(windowId : Int, inventory : PlayerInventory, player : PlayerEntity): Container =
    Register.INSCRIBER_MENU_TYPE.create(windowId, inventory)

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityEnergy.ENERGY) return energyLazy.cast()

    if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      val sideData = InscriberDirectionSettingsServer.getData

      if (sideData == 0 && side == DOWN) return this.inventoryCap(sideData).cast()
      else if (sideData == 1 && side == UP) return this.inventoryCap(sideData).cast()
      else if (sideData == 2 && side == NORTH) return this.inventoryCap(sideData).cast()
      else if (sideData == 3 && side == SOUTH) return this.inventoryCap(sideData).cast()
      else if (sideData == 4 && side == WEST) return this.inventoryCap(sideData).cast()
      else if (sideData == 5 && side == EAST) return this.inventoryCap(sideData).cast()
    }

    super.getCapability(cap, side)
  }

  def getEnergy: InscribersEnergyStorage = {
    this.energy
  }

  /*
  -------------------------------------------------------------------------
  | 0 -> Down | 1 -> Up | 2 -> North | 3 -> South | 4 -> West | 5 -> East |
  _________________________________________________________________________
  */
  def canExtractBySlot(slot: Int, direction: Direction): Boolean = {
    if (direction == null)
      return true
    if (slot == 0) {
      val side = InscriberDirectionSettingsServer.getData
      val enabled = InscriberDirectionSettingsServer.getEnabled

      if (side == 0 && direction == DOWN) return enabled
      else if (side == 1 && direction == UP) return enabled
      else if (side == 2 && direction == NORTH) return enabled
      else if (side == 3 && direction == SOUTH) return enabled
      else if (side == 4 && direction == WEST) return enabled
      else if (side == 5 && direction == EAST) return enabled
    }
    false
  }

  def getProgress: Int = {
    this.progress
  }

  def getTime: Int = if (this.recipe == null) 0 else this.recipe.getTime
}
