package com.napoleonit.uxrocket.data.models.local

import com.napoleonit.uxrocket.data.models.http.AttributeParameter

data class GetVariantsModel(
    val forItem: String,
    val parameters: List<AttributeParameter>? = null
)