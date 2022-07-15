package com.napoleonit.uxrocket.data.models.local

import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.shared.getCurrentDateString

class LogModel(
    val item: String,
    val itemName: String,
    val event: ContextEvent,
    val params: List<Param>? = null
) {
    var capturedDate: String = getCurrentDateString()
        private set
}