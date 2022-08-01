package com.napoleonit.uxrocket.data.models.local

import com.napoleonit.uxrocket.data.models.http.Action
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Класс предназачен для кэширования и чтения из кэшов.
 */

@Serializable
data class ParentElementModel(
    @SerialName("activity_or_fragment_name")  val activityOrFragmentName: String,
    @SerialName("elements")  val elements: List<ElementModel>
)

@Serializable
data class ElementModel(
    val id: Long,
    val campaignId: Long,
    val variantId: Long,
    var actions: List<Action>? = null
)