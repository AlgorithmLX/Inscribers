package com.algorithmlx.inscribers.server

object InscriberDirectionSettingsServer {
  private var data: Int = 0
  private var enabled: Boolean = false

  def getData: Int = this.data

  def setData(data: Int): Unit = {
    this.data = data
  }

  def getEnabled: Boolean = this.enabled

  def setEnabled(enabled: Boolean): Unit = {
    this.enabled = enabled
  }
}
