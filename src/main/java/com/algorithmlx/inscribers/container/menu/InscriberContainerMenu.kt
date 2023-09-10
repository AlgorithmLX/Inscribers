package com.algorithmlx.inscribers.container.menu

import com.algorithmlx.inscribers.api.handler.StackHandlerSlot
import com.algorithmlx.inscribers.block.InscriberBlockEntity
import com.algorithmlx.inscribers.container.InscriberCraftingContainer
import com.algorithmlx.inscribers.container.slot.InscriberResultSlot
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.init.registry.Register
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftResultInventory
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Container
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.IIntArray
import net.minecraft.util.IntArray
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandler

import net.minecraftforge.items.SlotItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

@Suppress("MemberVisibilityCanBePrivate")
class InscriberContainerMenu(
    windowId: Int,
    inventory: PlayerInventory,
    private val usedByPlayer: (PlayerEntity) -> Boolean,
    data: IIntArray,
    val pos: BlockPos
): Container(Register.inscriberContainerMenu.get(), windowId) {
    private val result: IInventory = CraftResultInventory()
    private val level: World = inventory.player.level

    init {
        val inv = InscriberBlockEntity().getInv()
        val craftInventory: IInventory = InscriberCraftingContainer(this, inv, 36)

        this.addSlot(InscriberResultSlot(this, craftInventory, this.result, 0, 79, 163))

        for (index in 1 until 36)
            for (i in 0 until 6)
                for (j in 0 until 6)
                    this.addSlot(StackHandlerSlot(inv, index, 36 + i * 18, 17 + j * 18))

        this.slotsChanged(craftInventory)
        this.addDataSlots(data)

        this.addPlayerInventory(inventory, 8, 198)
    }

    constructor(windowId: Int, inventory: PlayerInventory, packetBuffer: PacketBuffer):
            this(windowId, inventory, { _ -> false }, IntArray(36), packetBuffer.readBlockPos())

    override fun slotsChanged(pContainer: IInventory) {
        val recipe = this.level.recipeManager
            .getRecipeFor(InscribersRecipeTypes.inscriberRecipe, pContainer, this.level)
        if (recipe.isPresent) {
            val result = recipe.get().assemble(pContainer)
            this.result.setItem(0, result)
        } else this.result.setItem(0, ItemStack.EMPTY)

        super.slotsChanged(pContainer)
    }

    override fun stillValid(pPlayer: PlayerEntity): Boolean = this.usedByPlayer.invoke(pPlayer)

    override fun quickMoveStack(pPlayer: PlayerEntity, pIndex: Int): ItemStack = ItemStack.EMPTY

    fun addSlotRange(handler: IItemHandler?, index: Int, x: Int, y: Int, amount: Int, dx: Int): Int {
        var shadowindex = index
        var shadowx = x
        for (i in 0 until amount) {
            addSlot(SlotItemHandler(handler, shadowindex, shadowx, y))
            shadowx += dx
            shadowindex++
        }
        return shadowindex
    }

    fun addSlotBox(
        handler: IItemHandler?,
        index: Int,
        x: Int,
        y: Int,
        horAmount: Int,
        dx: Int,
        verAmount: Int,
        dy: Int
    ) {
        var shadowindex = index
        var shadowy = y
        for (j in 0 until verAmount) {
            shadowindex = addSlotRange(handler, shadowindex, x, shadowy, horAmount, dx)
            shadowy += dy
        }
    }

    fun addPlayerInventory(playerInventory: PlayerInventory, leftColX: Int, topRowY: Int) {
        var shadowtopRowY = topRowY
        addSlotBox(InvWrapper(playerInventory), 9, leftColX, shadowtopRowY, 9, 18, 3, 18)
        shadowtopRowY += 58
        addSlotRange(InvWrapper(playerInventory), 0, leftColX, shadowtopRowY, 9, 18)
    }
}