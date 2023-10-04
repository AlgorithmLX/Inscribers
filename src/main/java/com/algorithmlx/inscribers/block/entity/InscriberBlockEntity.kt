package com.algorithmlx.inscribers.block.entity

import com.algorithmlx.inscribers.LOGGER
import com.algorithmlx.inscribers.api.block.*
import com.algorithmlx.inscribers.api.energy.InscribersEnergyStorageAPI
import com.algorithmlx.inscribers.api.handler.*
import com.algorithmlx.inscribers.api.intArray
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.init.registry.*
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.CapabilityItemHandler

class InscriberBlockEntity : ContainerBlockEntity(Register.inscriberBlockEntity.get()), IInscriberBlockEntity {
    private val handler: StackHandler
    val energy: InscribersEnergyStorageAPI

    var progress: Int = 0
    private var isWorking: Boolean = false
    private var recipe: InscriberRecipe? = null

    private val energyLazy: LazyOptional<IEnergyStorage> = LazyOptional.of(this::energy)

    init {
        handler = StackHandler(36) {
            LOGGER.debug("Inventory for '{}' loaded", this::class.java)
            this.change()
        }
        energy = InscribersEnergyStorageAPI(this.getInscriber().getEnergy()) {
            LOGGER.debug("Energy storage for '{}' loaded", this::class.java)
        }
    }

    override fun getInv(): StackHandler = this.handler

    override fun getInscriber(): IInscriber = Register.inscriberBlock.get()

    override fun load(pState: BlockState, pCompound: CompoundNBT) {
        super.load(pState, pCompound)
        this.progress = pCompound.getInt("progress")
        this.energy.setStored(pCompound.getInt("energy"))
        this.isWorking = pCompound.getBoolean("working")
        LOGGER.debug("Data '{}' loaded", pCompound)
    }

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        val tag = super.save(pCompound)
        tag.putInt("progress", this.progress)
        tag.putInt("energy", this.energy.energyStored)
        tag.putBoolean("working", this.isWorking)
        LOGGER.debug("Data '{}' saved", pCompound)
        return tag
    }

    override fun tick() {
        val level = this.getLevel()
        if (level == null || level.isClientSide) return

        var change = false

        if (this.recipe == null || !this.recipe!!.matches(handler)) {
            val locRecipe = level.recipeManager.getRecipeFor(InscribersRecipeTypes.inscriberRecipe, this.handler.toContainer(), level)
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
                    for (j in 0 until 6)
                        for (k in 0 until 6) this.handler.extract(k + j * 6, 1, false)
                    this.handler.setStackInSlot(36, this.recipe!!.result(this.handler))
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

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (!this.isRemoved && cap == CapabilityEnergy.ENERGY) {
            LOGGER.debug("Energy capability for '{}' initialized", this::class.java)
            return CapabilityEnergy.ENERGY.orEmpty(cap, this.energyLazy)
        } else if (!this.isRemoved && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            LOGGER.debug("Item capability for '{}' initialized", this::class.java)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.capabilityItemLazy)
        }

        return super.getCapability(cap, side)
    }

    fun getTime(): Int = if (this.recipe != null) this.recipe!!.time else 0

    override fun createMenu(windowId : Int, inventory : PlayerInventory, player : PlayerEntity): Container =
        InscriberContainerMenu(windowId, inventory, this::usedByPlayer, intArray(0), this.blockPos)
}