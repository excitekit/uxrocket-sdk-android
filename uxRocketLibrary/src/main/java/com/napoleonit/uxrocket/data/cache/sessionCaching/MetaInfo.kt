package com.napoleonit.uxrocket.data.cache.sessionCaching

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.napoleonit.uxrocket.shared.UXRocketConstants.DEVICE_TYPE
import com.napoleonit.uxrocket.shared.UXRocketConstants.OS_NAME
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


class MetaInfo(
    appContext: Context,
    override val authKey: String,
    override val appRocketId: String
) : IMetaInfo {
    override val osName: String = OS_NAME
    override val deviceType: String = DEVICE_TYPE
    override val resolution: String = getScreenResolution(appContext)
    override val deviceID: String = getDeviceId(appContext)
    override val osVersion: String = getOSVersion()
    override val deviceLocale: String = getLocale()
    override val appPackageName: String = getPackageName(appContext)
    override val appVersionName: String = getAppVersionName(appContext)
    override val deviceModelName: String = getDeviceName()
    override val visitor: String get() = convertDeviceIdToMD5(deviceID)
    override val session: String = UUID.randomUUID().toString()
    override val operatorName: String? = getOperatorName(appContext)
    override val timeZoneName: String = getTimeZoneName()
    override val timeZoneShift: Double = getTimeZoneShift()

    /**
     * Оптиональные параметры
     */
    override var country: String? = null
    override var city: String? = null
    override var referrer: String? = null
    override var advertisingId: String? = getAdvertisingId(appContext)
    override fun setCountryAndCity(country: String, city: String) {
        this.country = country
        this.city = city
    }

    override fun setReferrerUrl(referrer: String) {
        this.referrer = referrer
    }
    override fun setAdvertising(advertisingId: String?) {
        this.advertisingId = advertisingId;
    }
}

private fun getAdvertisingId(context: Context) : String? {
    val sharedPreferences = context.getSharedPreferences("AdvertisingPrefs", Context.MODE_PRIVATE)
    var gaid = sharedPreferences.getString("GAID", null);
    return gaid
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

private fun getTimeZoneName(): String{
    return TimeZone.getDefault().displayName
}

private fun getTimeZoneShift(): Double{
    val timeZone = TimeZone.getDefault()
    return timeZone.getOffset(System.currentTimeMillis()) / (60.0 * 60.0 * 1000.0)
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

private fun getScreenResolution(context: Context): String {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display = wm.defaultDisplay
    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    val width = metrics.widthPixels
    val height = metrics.heightPixels
    return width.toString() + "x" + height.toString()
}

private fun convertDeviceIdToMD5(deviceId: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(deviceId.toByteArray())).toString(16).padStart(32, '0')
}

private fun getOperatorName(context: Context): String? {
    return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
}