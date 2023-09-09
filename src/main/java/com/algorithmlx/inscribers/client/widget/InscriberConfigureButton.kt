package com.algorithmlx.inscribers.client.widget

import com.algorithmlx.inscribers.api.client.InscribersButtonAPI
import com.algorithmlx.inscribers.network.InscribersNetwork
import com.algorithmlx.inscribers.network.packet.SDirectionPack
import com.algorithmlx.inscribers.reloc
import net.minecraft.util.text.StringTextComponent

class InscriberConfigureButton(
    x: Int,
    y: Int,
    id: Int
): InscribersButtonAPI(
    x,
    y,
    16,
    16,
    id,
    StringTextComponent(""),
    onPress = {
        if (getActivated()) {
            setActivated(false)
            InscribersNetwork.sendToServer(SDirectionPack(id, getActivated()))
        }

        if (getActivated(id)) {
            setActivated(false, id)
            InscribersNetwork.sendToServer(SDirectionPack(id, false))
        } else {
            setActivated(true, id)
            InscribersNetwork.sendToServer(SDirectionPack(id, true))
        }
    },
    texture = reloc("textures/gui/button/inscriber_button.png")
)