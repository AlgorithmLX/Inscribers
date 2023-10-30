package com.algorithmlx.inscribers.init.registry

import com.algorithmlx.inscribers.LOGGER
import com.algorithmlx.inscribers.api.*
import com.algorithmlx.inscribers.api.block.IInscriber
import com.algorithmlx.inscribers.client.render.InscriberBlockEntityRenderer
import com.algorithmlx.inscribers.client.screen.InscriberMenuScreen
import net.minecraft.client.gui.ScreenManager
import net.minecraft.item.BlockItem
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TextFormatting
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
                val x: ITextComponent = block.getXSize().stylizedInt()
                val y: ITextComponent = block.getYSize().stylizedInt()
                val type = block.getType().getTranslationName()
                val capacity: Int = block.getEnergy()
                val level = block.getTier().getTranslationName()

                tooltip.add(translate("api", "container", x, y))
                tooltip.add(translate("api", "capacity", capacity))
                tooltip.add(translate("api", "type", type))
                tooltip.add(translate("api", "level", level))
            }
        }
    }

    private fun Int.stylizedInt(): ITextComponent = StringTextComponent("$this").withStyle(TextFormatting.GOLD)
}