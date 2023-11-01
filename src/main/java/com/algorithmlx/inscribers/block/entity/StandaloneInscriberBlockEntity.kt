package com.algorithmlx.inscribers.block.entity

import com.algorithmlx.inscribers.api.block.ContainerBlockEntity
import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.api.block.IInscriberBlockEntity
import com.algorithmlx.inscribers.api.energy.InscribersEnergyStorageAPI
import com.algorithmlx.inscribers.api.handler.StackHandler
import com.algorithmlx.inscribers.api.helper.StackHelper
import com.algorithmlx.inscribers.api.intArray
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.init.config.InscribersConfig
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.init.registry.Register
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

class StandaloneInscriberBlockEntity(private val inscriber: IInscriber) : ContainerBlockEntity(Register.inscriberBlockEntity.get()), IInscriberBlockEntity {
    private val handler: StackHandler
    private val craftingHandler: StackHandler
    private val energy: InscribersEnergyStorageAPI

    private var recipe: InscriberRecipe? = null
    var progress: Int = 0
    private var isWorking: Boolean = false
    private var oldEnergy: Int = 0
    private var gridChanged = false
    private var timeOperation: Int = 0
    private var energyOperation: Int = 0

    constructor(): this(Register.basicInscriberBlock.get())

    init {
        this.handler = StackHandler(this.getInscriber().getSize(), this::change)
        this.craftingHandler = StackHandler(this.getInscriber().getSize() - 1)
        this.energy = InscribersEnergyStorageAPI(InscribersConfig.inscriberCapacity.get())

        this.handler.setOutputSlots(36)
        this.handler.setValidator(::canInsertStack)
    }

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        val tag = super.save(pCompound)
        tag.putInt("progress", this.progress)
        tag.putBoolean("working", this.isWorking)
        tag.putInt("energy", this.energy.energyStored)

        return tag
    }

    override fun load(pState: BlockState, pCompound: CompoundNBT) {
        super.load(pState, pCompound)
        this.progress = pCompound.getInt("progress")
        this.energy.setStored(pCompound.getInt("energy"))
        this.isWorking = pCompound.getBoolean("working")
    }

    override fun tick() {
        var changeBlockEntity = false
        val level = this.getLevel()
        val energy = this.getEnergyStorage()

        if (level != null) {
            if (this.isWorking) {
                this.updateInventory()
                val recipeContainer = this.getCraftingInventory().toContainer()

                if (this.gridChanged && (this.recipe == null || !this.recipe!!.matches(recipeContainer, level))) {
                    val recipe = level.recipeManager.getRecipeFor(InscribersRecipeTypes.matrixInscriberRecipe, recipeContainer, level).orElse(null)

                    this.recipe = recipe

                    this.gridChanged = false
                }

                if (!level.isClientSide) {
                    if (this.recipe != null) {
                        val localRecipe = this.recipe!!
                        this.timeOperation = if (this.recipe != null) recipe!!.time else 0
                        this.energyOperation = if (this.recipe != null) recipe!!.energyPerTick else 0
                        val inv = this.getInv()
                        val result = localRecipe.result(recipeContainer)
                        val outputSlot = inv.slots - 1
                        val output = inv.getStackInSlot(outputSlot)
                        val tier = this.getInscriber().getTier()

                        this.energyOperation = (this.energyOperation * tier.energyCostMultiplier).toInt()

                        this.timeOperation = if (tier.timeBoost < 0) (this.timeOperation * (tier.timeBoost * -1)).toInt()
                        else (this.timeOperation / tier.timeBoost).toInt()

                        if (StackHelper.canCombine(result, output) && energy.energyStored >= this.energyOperation) {
                            this.progress++
                            energy.extractEnergy(this.energyOperation, false)
                            if (this.progress >= this.timeOperation) {
                                val remaining = localRecipe.getRemainingItems(recipeContainer)
                                for (i in 0 until recipeContainer.containerSize) {
                                    if (!remaining[i].isEmpty) inv.setStackInSlot(i, remaining[i])
                                    else inv.extract(i, 1, false)
                                }

                                this.updateResult(result, outputSlot)
                                this.progress = 0
                                this.gridChanged = true
                            }

                            changeBlockEntity = true
                        }
                    } else {
                        if (progress > 0) {
                            this.progress = 0
                            changeBlockEntity = true
                        }
                    }
                }
            } else {
                if (this.progress > 0) {
                    this.progress = 0
                    changeBlockEntity = true
                }
            }

            val insertPowerRate = InscribersConfig.inscriberPowerInsert.get()

            if (!level.isClientSide && this.getEnergyStorage().energyStored >= insertPowerRate) {
                this.aboveInventory().ifPresent { handler ->
                    for (i in 0 until handler.slots) {
                        val stack = handler.getStackInSlot(i)
                        if (!stack.isEmpty && !handler.extractItem(i, 1, true).isEmpty) {
                            handler.extractItem(i, 1, false)
                            break
                        }
                    }
                }
            }
        }

        if (this.oldEnergy != energy.energyStored) {
            this.oldEnergy = energy.energyStored
            if (!changeBlockEntity) changeBlockEntity = true
        }

        if (changeBlockEntity) {
            this.change()
        }
    }

    override fun getInv(): StackHandler = this.handler
    override fun getCraftingInventory(): StackHandler = this.craftingHandler

    override fun getInscriber(): IInscriber = this.inscriber

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> =
        this.getEnergyCapability(this.isRemoved, cap, super.getCapability(cap, side))

    fun getTime() = this.timeOperation

    fun getOperationEnergy() = this.energyOperation

    override fun getEnergyStorage(): IEnergyStorage = this.energy

    private fun updateInventory() {
        val inventory = this.getInv()
        this.getCraftingInventory().setSize(inventory.slots - 1)
        for (i in 0 until inventory.slots - 1) {
            val stack = inventory.getStackInSlot(i)
            this.getCraftingInventory().setStackInSlot(i, stack)
        }
    }

    private fun updateResult(stack: ItemStack, slot: Int) {
        val inv = this.getInv()
        val result = inv.getStackInSlot(inv.slots - 1)
        if (result.isEmpty) inv.setStackInSlot(slot, stack)
        else inv.setStackInSlot(slot, StackHelper.grow(result, stack.count))
    }

    fun aboveInventory(): LazyOptional<IItemHandler> {
        val level = this.level
        val pos = this.blockPos.above()

        if (level != null) {
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity != null)
                return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN)
        }

        return LazyOptional.empty()
    }

    fun canInsertStack(slot: Int, stack: ItemStack): Boolean = true

    override fun createMenu(windowId : Int, inventory : PlayerInventory, player : PlayerEntity): Container =
        InscriberContainerMenu(windowId, inventory, this::usedByPlayer, this.getInv(), intArray(this.getInscriber().getSize()), this.blockPos)
}
