package com.algorithmlx.inscribers.init

import com.algorithmlx.inscribers.Constant
import com.tterrag.registrate.Registrate

object Register {
  private val reg = Registrate.create(Constant.ModId)

  def init(): Unit = {}
}
