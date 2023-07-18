package com.napoleonit.uxrocket.data.models.http

import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.local.CountingType
import com.napoleonit.uxrocket.data.models.local.ElementModel
import com.napoleonit.uxrocket.data.models.local.ParentElementModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetVariantsRequestModel(
    @SerialName("auth_key") val authKey: String,
    @SerialName("app_rocket_id") val appRocketID: String,
    @SerialName("os_name") val osName: String,
    @SerialName("device_type") val deviceType: String,
    @SerialName("app_version_name") val appVersionName: String,
    @SerialName("app_package_name") val appPackageName: String,
    @SerialName("operator_name") val operatorName: String? = null,
    @SerialName("resolution") val resolution: String,
    @SerialName("device_locale") val deviceLocale: String,
    @SerialName("city") val city: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("item") val item: String,
    @SerialName("session_Id") val session: String,
    @SerialName("visitor") val visitor: String,
    @SerialName("params") val params: List<AttributeParameter>? = null,
    @SerialName("elements") val elements: List<ElementModel>? = null,
    @SerialName("referrer") val referrer: String? = null
) {
    companion object {
        fun bindRequestModel(
            item: String,
            metaInfo: IMetaInfo,
            parameters: List<AttributeParameter>? = null,
            parentElementModel: ParentElementModel? = null,
        ) = GetVariantsRequestModel(
            authKey = metaInfo.authKey,
            appRocketID = metaInfo.appRocketId,
            osName = metaInfo.osName,
            deviceLocale = metaInfo.deviceLocale,
            appVersionName = metaInfo.appVersionName,
            appPackageName = metaInfo.appPackageName,
            deviceType = metaInfo.deviceType,
            operatorName = metaInfo.operatorName,
            city = metaInfo.city,
            referrer = metaInfo.referrer,
            country = metaInfo.country,
            resolution = metaInfo.resolution,
            session = metaInfo.session,
            visitor = metaInfo.visitor,
            elements = parentElementModel?.elements,
            item = item,
            params = parameters
        )
    }
}

@Serializable
data class Campaign(
    @SerialName("id") val id: Long,
    @SerialName("variants") val variants: List<Variant>,
    @SerialName("actions") val actions: List<Action>,
)

fun Campaign.bindVariantsForRequest(): Map<String, Long?> {
    val variants = HashMap<String, Long?>()
    val sortedVariants = this.variants.sortedBy { it.elementID }
    val elementIdsValues = (0..9)
    elementIdsValues.map { idValue ->
        val indexIdValue = idValue + 1
        variants["element_" + if (idValue / 9 != 1) "0${indexIdValue}" else "$indexIdValue"] =
            sortedVariants.getOrNull(idValue)?.id
    }
    return variants.toSortedMap(compareBy { it.replace("[^0-9]".toRegex(), "").toInt() })
}

@Serializable
data class Action(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("item") val item: String,
    @SerialName("action_type") val actionType: Type,
    @SerialName("counting_type") val countingType: CountingType,
) {
    @Serializable
    enum class Type {
        @SerialName("1")
        CLICK,

        @SerialName("0")
        SHOW,

        @SerialName("2")
        ANY
    }
}

@Serializable
data class Variant(
    @SerialName("id") val id: Long,
    @SerialName("element_id") val elementID: Long,
    @SerialName("variant_attrs") val variantAttrs: List<VariantAttr>? = null,
)

@Serializable
data class VariantAttr(
    @SerialName("item") val item: String,
    @SerialName("attributes") val attributes: List<Attribute>,
)