package com.algorithmlx.inscribers.api.container

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

abstract class AbstractContainerMenu(pMenuType: ContainerType<*>?, pContainerId: Int) : Container(pMenuType, pContainerId) {
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