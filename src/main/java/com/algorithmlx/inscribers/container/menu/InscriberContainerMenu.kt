package com.algorithmlx.inscribers.container.menu

import com.algorithmlx.inscribers.api.container.AbstractContainerMenu
import com.algorithmlx.inscribers.api.handler.StackHandler
import com.algorithmlx.inscribers.api.handler.StackHandlerSlot
import com.algorithmlx.inscribers.api.intArray
import com.algorithmlx.inscribers.block.entity.StandaloneInscriberBlockEntity
import com.algorithmlx.inscribers.container.InscriberCraftingContainer
import com.algorithmlx.inscribers.container.slot.InscriberResultSlot
import com.algorithmlx.inscribers.init.registry.InscribersRecipeTypes
import com.algorithmlx.inscribers.init.registry.Register
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftResultInventory
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.IIntArray
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

@Suppress("MemberVisibilityCanBePrivate")
class InscriberContainerMenu(
    windowId: Int,
    inventory: PlayerInventory,
    private val usedByPlayer: (PlayerEntity) -> Boolean,
    inv: StackHandler,
    data: IIntArray,
    pos: BlockPos
): AbstractContainerMenu(Register.inscriberContainerMenu.get(), windowId, pos) {
    private val result: IInventory
    private val level: World

    init {
        this.result = CraftResultInventory()
        this.level = inventory.player.level

        val craftInv = InscriberCraftingContainer(this, inv, 36)

        this.addSlot(InscriberResultSlot(this, craftInv, this.result, 36, 79, 162))

        for (j in 0 until 6) {
            for (k in 0 until 6) {
                this.addSlot(StackHandlerSlot(inv, k + j * 6, 34 + j * 18, 17 + k * 18))
            }
        }

        this.slotsChanged(craftInv)
        this.addDataSlots(data)

        this.addPlayerInventory(inventory, 8, 198)
    }

    constructor(windowId: Int, inventory: PlayerInventory, packetBuffer: PacketBuffer):
            this(windowId, inventory, { _ -> false }, StandaloneInscriberBlockEntity().getInv(), intArray(36), packetBuffer.readBlockPos())

    override fun slotsChanged(pContainer: IInventory) {
        super.slotsChanged(pContainer)
    }

    override fun stillValid(pPlayer: PlayerEntity): Boolean = this.usedByPlayer.invoke(pPlayer)

    override fun quickMoveStack(pPlayer: PlayerEntity, pIndex: Int): ItemStack = ItemStack.EMPTY
}