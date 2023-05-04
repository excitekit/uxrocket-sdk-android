package com.napoleonit.uxrocket.data.models.http

import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.local.CountingType
import com.napoleonit.uxrocket.data.models.local.LogCampaignModel
import com.napoleonit.uxrocket.data.models.local.LogIdentityModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class SaveIdentityMatchModel(
    @SerialName("auth_key") val authKey: String,
    @SerialName("match_type") val matchType: Int,
    @SerialName("identity_1") val identity1: Int? = null,
    @SerialName("value_1") val value1: String? = null,
    @SerialName("identity_2") val identity2: Int? = null,
    @SerialName("value_2") val value2: String? = null,
    @SerialName("identity_3") val identity3: Int? = null,
    @SerialName("value_3") val value3: String? = null,
) {

    companion object {
        fun bindRequestModel(
            model: LogIdentityModel,
            metaInfo: IMetaInfo,
        ) = SaveIdentityMatchModel(
            authKey = metaInfo.authKey,
            matchType = model.matchType,
            identity1 = model.type1,
            value1 = model.value1,
            identity2 = model.type2,
            value2 = model.value2,
            identity3 = model.type3,
            value3 = model.value3,
        )
    }
}
