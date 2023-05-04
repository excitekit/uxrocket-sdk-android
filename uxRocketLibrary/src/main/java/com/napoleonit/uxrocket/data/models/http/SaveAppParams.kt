package com.napoleonit.uxrocket.data.models.http

import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveRawAppDataRequestModel(
    @SerialName("auth_key") val authKey: String,
    @SerialName("app_rocket_id") val appRocketID: String,
    @SerialName("item_name") val itemName: String,
    @SerialName("item") val item: String,
    @SerialName("capture_dt") val captureDt: String? = null,
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
    @SerialName("event_context") val eventContext: ContextEvent? = null,
    @SerialName("visitor") val visitor: String,
    @SerialName("product_code") val productCode: String? = null,
    @SerialName("product_price") val productPrice: Double? = null,
    @SerialName("product_count") val productCount: Int? = null,
    @SerialName("cart_sum") val cartSum: Double? = null,
    @SerialName("session_id") val session: String,
    @SerialName("params") val params: List<AttributeParameter>? = null
) {
    companion object {
        fun bindRequestModel(
            model: LogModel,
            metaInfo: IMetaInfo
        ) = SaveRawAppDataRequestModel(
            authKey = metaInfo.authKey,
            appRocketID = metaInfo.appRocketId,
            osName = metaInfo.osName,
            osVersion = metaInfo.osVersion,
            deviceLocale = metaInfo.deviceLocale,
            deviceID = metaInfo.deviceID,
            deviceModel = metaInfo.deviceModelName,
            appVersionName = metaInfo.appVersionName,
            appPackageName = metaInfo.appPackageName,
            deviceType = metaInfo.deviceType,
            city = metaInfo.city,
            country = metaInfo.country,
            operatorName = metaInfo.operatorName,
            resolution = metaInfo.resolution,
            visitor = metaInfo.visitor,
            session = metaInfo.session,
            connectionType = model.connectionType,
            item = model.item,
            productCode = model.productCode,
            productPrice = model.productPrice,
            productCount = model.productCount,
            cartSum = model.cartSum,
            itemName = model.itemName,
            captureDt = model.capturedDate,
            eventContext = model.event,
            params = model.parameters
        )
    }
}

@Serializable
class AttributeParameter(
    @SerialName("id") val id: String,
    @SerialName("value") val value: String
)


@Serializable
enum class ContextEvent {
    @SerialName("install")
    INSTALL,

    @SerialName("openpage")
    OPEN_PAGE,

    @SerialName("mainmenu")
    MAIN_MENU,

    @SerialName("buttons")
    BUTTONS,

    @SerialName("popup")
    POPUP,

    @SerialName("links")
    LINKS,

    @SerialName("input")
    INPUT;

    override fun toString(): String {
        return this.name.lowercase()
    }
}