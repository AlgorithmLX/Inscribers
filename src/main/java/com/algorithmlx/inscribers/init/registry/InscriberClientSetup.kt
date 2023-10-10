package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.LOGGER
import com.algorithmlx.inscribers.api.*
import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.client.render.InscriberBlockEntityRenderer
import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen
import net.minecraft.client.gui.ScreenManager
import net.minecraft.item.BlockItem
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

object InscriberClientSetup {
    @JvmStatic
    fun setupClient() {
        modBus.addListener(this::clientSetup)

        if (isPhysicalClient()) {
            forgeBus.addListener(this::tooltipEvent)
        }
    }

    private fun clientSetup(evt: FMLClientSetupEvent) {
        LOGGER.info("Client-side initialized")

        ScreenManager.register(Register.inscriberContainerMenu.get(), ::InscriberMenuScreen)

        ClientRegistry.bindTileEntityRenderer(Register.inscriberBlockEntity.get(),
            ::InscriberBlockEntityRenderer
        )
    }

    @Synchronized
    private fun tooltipEvent(evt: ItemTooltipEvent) {
        val stack = evt.itemStack
        val tooltip = evt.toolTip
        val item = stack.item

        if (item is BlockItem) {
            val block = item.block

            if (block is IInscriber) {
                val x: Int = block.getXSize()
                val y: Int = block.getYSize()
                val type = block.getType().getTranslationName()
                val capacity: Int = block.getEnergy()

                tooltip.add(TranslationTextComponent(basedText("api", "container"), x, y))
                tooltip.add(TranslationTextComponent(basedText("api", "capacity"), capacity))
                tooltip.add(TranslationTextComponent(basedText("api", "type"), type))
            }
        }
    }
}