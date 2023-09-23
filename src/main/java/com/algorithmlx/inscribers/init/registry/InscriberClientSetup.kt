package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.LOGGER
import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.api.forgeBus
import com.algorithmlx.inscribers.api.isPhysicalClient
import com.algorithmlx.inscribers.api.modBus
import com.algorithmlx.inscribers.api.translate
import com.algorithmlx.inscribers.client.render.InscriberBlockEntityRenderer
import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen
import net.minecraft.client.gui.ScreenManager
import net.minecraft.item.BlockItem
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
        val player = evt.player ?: return
        val tooltip = evt.toolTip
        val item = stack.item

        if (item is BlockItem) {
            val block = item.block

            if (block is IInscriber) {
                val x = block.getXSize()
                val y = block.getYSize()
                val type = block.getType().getTranslationName()
                val capacity = block.getEnergy()

                synchronized(player) {
                    if (player.isShiftKeyDown) {
                        tooltip.add(translate("api", "container", x, y))
                        tooltip.add(translate("api", "capacity", capacity))
                        tooltip.add(translate("api", "type", type))
                    } else {
                        tooltip.add(translate("api", "shift"))
                    }
                }
            }
        }
    }
}