package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.api.*
import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.client.render.InscriberBlockEntityRenderer
import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen
import com.algorithmlx.inscribers.compact.CompactInitializer
import com.algorithmlx.inscribers.init.config.InscribersConfig
import com.algorithmlx.inscribers.network.InscribersNetwork
import net.minecraft.client.gui.ScreenManager
import net.minecraft.item.BlockItem
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

object InscriberStartup {
    @JvmStatic
    fun init() {
        InscribersNetwork.messageRegister()
        Register.init()
        CompactInitializer.init()
        forgeBus.addListener(this::clientTickEvent)
        modBus.addListener(this::clientInit)
        makeConfig(side = "common", spec = InscribersConfig.spec)
    }

    private fun clientTickEvent(evt: ClientTickEvent) {
        forgeBus.addListener(this::tooltipEvent)
    }

    private fun clientInit(evt: FMLClientSetupEvent) {
        ScreenManager.register(Register.inscriberContainerMenu.get(), ::InscriberMenuScreen)
        ClientRegistry.bindTileEntityRenderer(Register.inscriberBlockEntity.get(), ::InscriberBlockEntityRenderer)
    }

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