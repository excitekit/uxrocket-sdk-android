package com.napoleonit.uxrocket.data.models.http

import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
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
    @SerialName("params") val params: List<AttributeParameter>? = null,
    @SerialName("elements") val elements: List<ElementModel>? = null,
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
            country = metaInfo.country,
            resolution = metaInfo.resolution,
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
) {
    companion object {
        fun bindVariantsForRequest(campaign: Campaign): Map<String, Long> {
            val variants = HashMap<String, Long>()
            val sortedVariants = campaign.variants.sortedBy { it.elementID }
            sortedVariants.forEach { variant ->
                variants["element_${variant.elementID}"] = variant.id
            }
            return variants.toSortedMap(compareBy { it.replace("[^0-9]".toRegex(), "").toInt() })
        }
    }
}

@Serializable
data class Action(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("item") val item: String,
    @SerialName("action_type") val actionType: Type,
    @SerialName("counting_type") val countingType: Long,
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