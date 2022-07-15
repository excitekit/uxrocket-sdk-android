package com.napoleonit.uxrocket.data.models.local

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Polymorphic
sealed class Param {
    abstract val id: String

    @Serializable
    class StringParam(
        @SerialName("id") override val id: String,
        @SerialName("value") val value: String
    ) : Param()

    class BooleanParam(
        @SerialName("id") override val id: String,
        @SerialName("value") val value: Boolean
    ) : Param()
}