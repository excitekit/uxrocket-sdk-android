package com.napoleonit.uxrocket.data.models.local

import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.shared.getCurrentDateString
import kotlinx.serialization.SerialName
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

@Serializable
class LogCampaignModel(
    val campaignId: String,
    val actionName: String,
    val totalValue: Int,
    val countingType: CountingType,
    val variants: HashMap<String, String>,
    val params: List<AttributeParameter>? = null
) {
    var capturedDate: String = getCurrentDateString()
        private set
}

@Serializable
enum class CountingType {
    @SerialName("0")
    SHOW,

    @SerialName("1")
    COUNTING_PARAMETER,

    @SerialName("2")
    COUNTING_PARAMETER_2
}