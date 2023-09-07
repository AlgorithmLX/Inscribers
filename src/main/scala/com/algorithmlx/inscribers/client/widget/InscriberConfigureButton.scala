package com.algorithmlx.inscribers.client.widget

import com.algorithmlx.inscribers.Constant.reloc
import com.algorithmlx.inscribers.api.client.InscribersButtonAPI
import com.algorithmlx.inscribers.network.InscribersNetwork
import com.algorithmlx.inscribers.network.packet.SDirectionPack
import net.minecraft.util.text.StringTextComponent

class InscriberConfigureButton(
  x: Int,
  y: Int,
  id: Int
) extends InscribersButtonAPI(
  x,
  y,
  16,
  16,
  id,
  new StringTextComponent(""),
  button => {
    if (InscribersButtonAPI.getActivated) {
      InscribersButtonAPI.setActivated(false)
      InscribersNetwork.sendToServer(new SDirectionPack(InscribersButtonAPI.getId, InscribersButtonAPI.getActivated))
    }

    if (InscribersButtonAPI.getActivated(id)) {
      InscribersButtonAPI.setActivated(value = false, id)
      InscribersNetwork.sendToServer(new SDirectionPack(id, false))
    } else {
      InscribersButtonAPI.setActivated(value = true, id)
      InscribersNetwork.sendToServer(new SDirectionPack(id, true))
    }
  },
  reloc("textures/gui/button/inscriber_button.png")
) {}
