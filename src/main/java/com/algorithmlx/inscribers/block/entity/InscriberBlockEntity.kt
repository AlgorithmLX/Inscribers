package com.algorithmlx.inscribers.block.entity

import com.algorithmlx.inscribers.api.block.*
import com.algorithmlx.inscribers.api.handler.*
import com.algorithmlx.inscribers.api.intArray
import com.algorithmlx.inscribers.container.menu.InscriberContainerMenu
import com.algorithmlx.inscribers.init.registry.*
import com.algorithmlx.inscribers.recipe.InscriberRecipe
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.util.IIntArray
import net.minecraftforge.items.ItemStackHandler

class InscriberBlockEntity : OpenBlockEntity(Register.inscriberBlockEntity.get()), IInscriberBlockEntity {
    private val handler = object : ItemStackHandler(36) {
        override fun onContentsChanged(slot: Int) {
            this@InscriberBlockEntity.setChanged()
        }
    }

    private val data: IIntArray
    private var progress: Int = 0
    private var recipe: InscriberRecipe? = null

    init {
        data = object : IIntArray {
            override fun get(pIndex: Int): Int {
                return when (pIndex) {
                    0 -> this@InscriberBlockEntity.progress
                    1 -> if (this@InscriberBlockEntity.recipe != null) recipe!!.time else 100
                    else -> 0
                }
            }

            override fun set(pIndex: Int, pValue: Int) {
                TODO("Not yet implemented")
            }

            override fun getCount(): Int {
                TODO("Not yet implemented")
            }

        }
    }

    override fun getInscriber(): IInscriber = Register.inscriberBlock.get()

    override fun tick() {
    }

    override fun createMenu(windowId : Int, inventory : PlayerInventory, player : PlayerEntity): Container =
        InscriberContainerMenu(windowId, inventory, { false }, data, this.blockPos)
}