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
  8,
  8,
  id,
  new StringTextComponent(""),
  _ => {
    if (InscribersButtonAPI.getActivated) InscribersButtonAPI.setActivated(false)

    if (InscribersButtonAPI.getActivated(id)) InscribersButtonAPI.setActivated(value = false, id)
    else {
      InscribersNetwork.sendToServer(new SDirectionPack(id))
      InscribersButtonAPI.setActivated(value = true, id)
    }
  },
  reloc("textures/gui/button/inscriber_button.png")
) {}
