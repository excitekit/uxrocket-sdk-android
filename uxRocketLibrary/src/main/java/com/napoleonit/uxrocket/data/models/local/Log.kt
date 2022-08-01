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
    var parameters: List<AttributeParameter>? = null
) {
    lateinit var connectionType: String

    var capturedDate: String = getCurrentDateString()
        private set
}

@Serializable
class LogCampaignModel(
    val campaignId: Long? = null,
    val totalValue: Int? = null,
    var parameters: List<AttributeParameter>? = null,
    val itemIdentificator: String? = null,
    val actionName: String? = null,
    val activityOrFragmentName: String,
    val variants: Map<String, Long> ?= null
) {
    var capturedDate: String = getCurrentDateString()
        private set

    var countingType: CountingType = CountingType.COUNTING_PARAMETER
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