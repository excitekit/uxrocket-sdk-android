package com.napoleonit.crmlibrary.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveRawAppParamsRequestModel(
    @SerialName("auth_key") val authKey: String,
    @SerialName("app_rocket_id") val appRocketID: String,
    @SerialName("event_context") val eventContext: String? = null,
    @SerialName("item_name") val itemName: String,
    @SerialName("item") val item: String,
    @SerialName("captured_dt") val capturedDt: String? = null,
    @SerialName("device_id") val deviceID: String,
    @SerialName("os_name") val osName: String,
    @SerialName("os_version") val osVersion: String,
    @SerialName("device_model") val deviceModel: String,
    @SerialName("device_type") val deviceType: String,
    @SerialName("device_locale") val deviceLocale: String,
    @SerialName("app_version_name") val appVersionName: String,
    @SerialName("app_package_name") val appPackageName: String,
    @SerialName("connection_type") val connectionType: String? = null,
    @SerialName("operator_name") val operatorName: String? = null,
    @SerialName("resolution") val resolution: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("params") val params: List<ParamRequestModel>? = null
) {
    companion object {
        fun bindModel(builder: SaveAppParamBuilder, metaDataEntity: UXRocketMetaDataEntity) = SaveRawAppParamsRequestModel(

            //Cached data part
            authKey = metaDataEntity.authKey,
            appRocketID = metaDataEntity.appRocketId,
            osName = metaDataEntity.osName,
            osVersion = metaDataEntity.osVersion,
            deviceLocale = metaDataEntity.deviceLocale,
            deviceID = metaDataEntity.deviceID,
            deviceModel = metaDataEntity.deviceModelName,
            appVersionName = metaDataEntity.appVersionName,
            appPackageName = metaDataEntity.appPackageName,
            deviceType = metaDataEntity.deviceType,

            //Builder part
            item = builder.item,
            itemName = builder.itemName,
            capturedDt = builder.capturedDate,

            )
    }
}

@Serializable
data class ParamRequestModel(
    @SerialName("id") val id: Long,
    @SerialName("value") val value: String
)