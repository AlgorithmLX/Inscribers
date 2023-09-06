package com.algorithmlx.inscribers.server

object InscriberDirectionSettingsServer {
  private var data: Int = 0
  def getData: Int = this.data
  def setData(data: Int): Unit = {
    this.data = data
  }
}
