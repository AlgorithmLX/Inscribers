package com.algorithmlx.inscribers.block.entity

import com.algorithmlx.inscribers.api.block.*
import com.algorithmlx.inscribers.api.handler.*
import com.algorithmlx.inscribers.api.intArray
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.api.energy.InscribersEnergyStorageAPI
import com.algorithmlx.inscribers.init.registry.*
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import com.algorithmlx.inscribers.server.InscriberDirectionSettings
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.CapabilityItemHandler
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.TestOnly

class InscriberBlockEntity: ContainerBlockEntity(Register.inscriberBlockEntity.get()), IInscriberBlockEntity {
    val energy: InscribersEnergyStorageAPI
    private val inventory: StackHandler
    private var isWorking = false

    init {
        energy = InscribersEnergyStorageAPI(this.getInscriber().getEnergy())
        inventory = StackHandler(this.getInscriber().getSize(), this::change)
    }

    private val energyLazy = LazyOptional.of(this::energy)

    @TestOnly
    @ApiStatus.Experimental
    @Deprecated("Deprecated as not valid work", level = DeprecationLevel.HIDDEN)
    private val exitsSides: IntArray = intArrayOf(0)

    private var progress: Int = 0
    private var recipe: InscriberRecipe? = null

    override fun getInv(): StackHandler = this.inventory

    override fun tick() {
        val level = this.getLevel()
        if (level == null || level.isClientSide) return

        var change = false

        if (this.recipe == null || !this.recipe!!.matches(inventory)) {
            val locRecipe = level.recipeManager.getRecipeFor(InscribersRecipeTypes.inscriberRecipe, this.inventory.toContainer(), level)
                .orElse(null)
            this.recipe = if (locRecipe is InscriberRecipe) locRecipe else null
        }

        if (this.recipe != null) {
            val needsEnergy = this.recipe!!.energyPerTick // Needs energy per tick
            val resultTime = this.recipe!!.time
            this.isWorking = true
            if (this.energy.energyStored >= needsEnergy) {
                this.progress += 1
                this.energy.extractEnergy(needsEnergy, simulate = false)

                if (this.progress >= resultTime) {
                    for (i in 1 ..< 36) this.inventory.extract(i, 1, simulate = false)
                    this.inventory.setStackInSlot(0, this.recipe!!.result(this.inventory))
                    this.progress = 0
                    this.isWorking = false
                    change = true
                }
            }
        } else {
            if (this.progress > 0) this.progress = 0
            change = true
        }

        if (change) this.change()
    }

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        super.save(pCompound)
        pCompound.putInt("InscriberProgress", this.progress)
        pCompound.putInt("InscriberEnergy", this.energy.energyStored)
        pCompound.putInt("InscriberExitSide", this.getData())
        pCompound.putBoolean("EnableInscriberInjecting", this.getInjecting())
        return pCompound
    }

    override fun load(state: BlockState, tag: CompoundNBT) {
        super.load(state, tag)
        this.progress = tag.getInt("InscriberProgress")
        this.energy.setStored(tag.getInt("InscriberEnergy"))
        this.setData(tag.getInt("InscriberExitSide"))
        this.setInjecting(tag.getBoolean("EnableInscriberInjecting"))
    }

    override fun createMenu(windowId : Int, inventory : PlayerInventory, player : PlayerEntity): Container =
        InscriberContainerMenu(windowId, inventory, this::usedByPlayer, intArray(0), this.blockPos)

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap == CapabilityEnergy.ENERGY) return this.energyLazy.cast()

//        if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            val sideData = InscriberDirectionSettingsServer.data
//
//            if (sideData == 0 && side == Direction.DOWN) return this.inventoryCap[sideData]!!.cast()
//            else if (sideData == 1 && side == Direction.UP) return this.inventoryCap[sideData]!!.cast()
//            else if (sideData == 2 && side == Direction.NORTH) return this.inventoryCap[sideData]!!.cast()
//            else if (sideData == 3 && side == Direction.SOUTH) return this.inventoryCap[sideData]!!.cast()
//            else if (sideData == 4 && side == Direction.WEST) return this.inventoryCap[sideData]!!.cast()
//            else if (sideData == 5 && side == Direction.EAST) return this.inventoryCap[sideData]!!.cast()
//        }

        if (!this.isRemoved && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return this.itemCap.cast()

        return super.getCapability(cap, side)
    }

    /*
    |-----------|---------|------------|------------|-----------|-----------|
    | 0 -> Down | 1 -> Up | 2 -> North | 3 -> South | 4 -> West | 5 -> East |
    |___________|_________|____________|____________|___________|___________|
    */
    private fun canExtractBySlot(slot: Int, direction: Direction?): Boolean {
        if (direction == null)
            return true
        if (slot == 0) {
            val side = InscriberDirectionSettings.data
            val enabled = InscriberDirectionSettings.enabled

            if (side == 0 && direction == Direction.DOWN) return enabled
            else if (side == 1 && direction == Direction.UP) return enabled
            else if (side == 2 && direction == Direction.NORTH) return enabled
            else if (side == 3 && direction == Direction.SOUTH) return enabled
            else if (side == 4 && direction == Direction.WEST) return enabled
            else if (side == 5 && direction == Direction.EAST) return enabled
        }
        return false
    }

    fun getProgress(): Int = this.progress

    fun getTime(): Int = if (this.recipe == null) 0 else this.recipe!!.time

    override fun getInscriber(): IInscriber = Register.inscriberBlock.get()

    fun getData(): Int = InscriberDirectionSettings.data

    fun getInjecting(): Boolean = InscriberDirectionSettings.enabled

    fun setData(value: Int) {
        InscriberDirectionSettings.data = value
    }

    fun setInjecting(value: Boolean) {
        InscriberDirectionSettings.enabled = value
    }
}