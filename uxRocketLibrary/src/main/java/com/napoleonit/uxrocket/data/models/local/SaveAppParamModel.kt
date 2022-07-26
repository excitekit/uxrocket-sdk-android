package com.napoleonit.uxrocket.data.models.local

import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.shared.getCurrentDateString
import kotlinx.serialization.Serializable

@Serializable
class LogModel(
    val item: String,
    val itemName: String,
    val event: ContextEvent,
    val params: List<AttributeParameter>? = null
) {
    lateinit var connectionType: String

    var capturedDate: String = getCurrentDateString()
        private set
}