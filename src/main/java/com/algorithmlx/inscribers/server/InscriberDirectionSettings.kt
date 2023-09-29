package com.algorithmlx.inscribers.server

object InscriberDirectionSettings {
    var data: Int = 0
    var enabled: Boolean = false

    @JvmStatic
    fun sendChanges(slot: Int, enabled: Boolean) {
        this.data = slot
        this.enabled = enabled
    }
}