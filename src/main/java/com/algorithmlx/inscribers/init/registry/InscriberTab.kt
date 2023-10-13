package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.reloc
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class InscriberTab private constructor(id: String) : ItemGroup(id) {
    override fun makeIcon(): ItemStack = ItemStack(Register.basicInscriberBlock.get())

    override fun getTabsImage(): ResourceLocation = reloc("textures/gui/tab/inscriber_design.png")

    override fun getBackgroundImage(): ResourceLocation = reloc("textures/gui/tab/inscriber_tab.png")

    companion object {
        @JvmStatic
        fun create(id: String): InscriberTab = InscriberTab(id)
    }
}