package com.algorithmlx.inscribers.init.registry

import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

class InscriberTab private constructor(id: String) : ItemGroup(id) {
    override fun makeIcon(): ItemStack = ItemStack(Register.inscriberBlock.get())

    companion object {
        fun create(id: String): InscriberTab = InscriberTab(id)
    }
}