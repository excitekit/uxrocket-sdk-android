package com.napoleonit.crmlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.napoleonit.crmlibrary.data.models.CrmMetaDataEntity
import com.napoleonit.crmlibrary.di.DI
import com.napoleonit.crmlibrary.shared.CRMConstants.NAME_SDK
import java.util.*


object GetCrm {
    @SuppressLint("HardwareIds")
    fun init(appContext: Context) {
        DI.init(appContext)
        try {
            with(appContext) {
                val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionName = packageInfo.versionName
                val deviceID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                val osVersion = Build.VERSION.SDK_INT.toString()
                val local = Locale.getDefault().language
                val cachingCrmMetaDataEntity = CrmMetaDataEntity(
                    appPackageName = packageName,
                    appVersionName = versionName,
                    deviceID = deviceID,
                    osVersion = osVersion,
                    deviceLocale = local,
                    osName = "Android"
                )
                Log.i(NAME_SDK, "Initialize completed")
            }
        } catch (e: Exception) {
            Log.e(NAME_SDK, e.message ?: "Initialize $NAME_SDK Error")
        }
    }
}