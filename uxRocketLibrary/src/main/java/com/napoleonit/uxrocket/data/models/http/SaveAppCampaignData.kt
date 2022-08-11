package com.napoleonit.uxrocket.data.models.http

import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.local.CountingType
import com.napoleonit.uxrocket.data.models.local.LogCampaignModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveRawAppCampaignDataRequestModel(
    @SerialName("auth_key") val authKey: String,
    @SerialName("app_rocket_id") val appRocketId: String,
    @SerialName("visitor") val visitor: String,
    @SerialName("capture_dt") val capturedDt: String,
    @SerialName("os_name") val osName: String,
    @SerialName("os_version") val osVersion: String,
    @SerialName("device_model") val deviceModel: String,
    @SerialName("device_type") val deviceType: String,
    @SerialName("device_locale") val deviceLocale: String,
    @SerialName("app_version_name") val appVersionMame: String,
    @SerialName("app_package_name") val appPackageName: String,
    @SerialName("resolution") val resolution: String,
    @SerialName("item") val item: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("campaign_id") val campaignId: Long? = null,
    @SerialName("action_name") val actionName: String? = null,
    @SerialName("counting_type") val countingType: CountingType?,
    @SerialName("total_value") val totalValue: Int? = null,
    @SerialName("params") val params: List<AttributeParameter>? = null,
    @SerialName("variants") val variants: Map<String, Long?>? = null,
) {

    companion object {
        fun bindRequestModel(
            model: LogCampaignModel,
            metaInfo: IMetaInfo,
        ) = SaveRawAppCampaignDataRequestModel(
            authKey = metaInfo.authKey,
            appRocketId = metaInfo.appRocketId,
            appVersionMame = metaInfo.appVersionName,
            osName = metaInfo.osName,
            osVersion = metaInfo.osVersion,
            deviceLocale = metaInfo.deviceLocale,
            deviceModel = metaInfo.deviceModelName,
            appPackageName = metaInfo.appPackageName,
            deviceType = metaInfo.deviceType,
            city = metaInfo.city,
            country = metaInfo.country,
            resolution = metaInfo.resolution,
            visitor = metaInfo.visitor,
            params = model.parameters,
            capturedDt = model.capturedDate,
            actionName = model.actionName,
            countingType = model.countingType,
            campaignId = model.campaignId,
            totalValue = model.totalValue,
            variants = model.variants,
            item = model.itemIdentificator
        )
    }
}
