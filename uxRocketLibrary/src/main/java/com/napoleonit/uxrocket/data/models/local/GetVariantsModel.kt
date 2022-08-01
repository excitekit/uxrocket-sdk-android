package com.napoleonit.uxrocket.data.models.local

import com.napoleonit.uxrocket.data.models.http.AttributeParameter

data class GetVariantsModel(
    val activityOrFragmentName: String,
    var parameters: List<AttributeParameter>? = null
)