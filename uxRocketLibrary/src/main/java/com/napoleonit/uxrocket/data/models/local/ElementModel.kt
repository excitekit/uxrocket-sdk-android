package com.napoleonit.uxrocket.data.models.local

import kotlinx.serialization.Serializable

/**
 * Класс предназачен для кэширования и чтения из кэшов.
 */

@Serializable
data class ParentElementModel(
    val fromItem: String,
    val elements: List<ElementModel>
)

@Serializable
data class ElementModel(
    val id: Long,
    val campaignId: Long,
    val variantId: Long
)