package com.napoleonit.uxrocket.data.sessionCaching

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.napoleonit.uxrocket.shared.UXRocketConstants.DEVICE_TYPE
import com.napoleonit.uxrocket.shared.UXRocketConstants.OS_NAME
import java.util.*


class MetaInfo(
    appContext: Context,
    override val authKey: String,
    override val appRocketId: String
) : IMetaInfo {
    override val osName: String = OS_NAME
    override val deviceType: String = DEVICE_TYPE
    override val deviceID: String = getDeviceId(appContext)
    override val osVersion: String = getOSVersion()
    override val deviceLocale: String = getLocale()
    override val appPackageName: String = getPackageName(appContext)
    override val appVersionName: String = getAppVersionName(appContext)
    override val deviceModelName: String = getDeviceName()

    /**
     * Оптиональные параметры
     */
    override var country: String? = null
    override var city: String? = null
    override fun setCountryAndCity(country: String, city: String) {
        this.country = country
        this.city = city
    }
}

private fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        model.replaceFirstChar(Char::titlecase)
    } else "${manufacturer.replaceFirstChar(Char::titlecase)} $model"
}

private fun getOSVersion(): String {
    return Build.VERSION.SDK_INT.toString()
}

private fun getLocale(): String {
    return Locale.getDefault().language
}

private fun getPackageName(appContext: Context): String {
    return appContext.packageName
}

private fun getAppVersionName(appContext: Context): String {
    val packageInfo = appContext.packageManager
        .getPackageInfo(appContext.packageName, 0)
    return packageInfo.versionName
}

@SuppressLint("HardwareIds")
private fun getDeviceId(appContext: Context): String {
    return Settings
        .Secure
        .getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)!!
}