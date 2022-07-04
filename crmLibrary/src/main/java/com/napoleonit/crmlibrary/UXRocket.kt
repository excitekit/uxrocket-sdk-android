package com.napoleonit.crmlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.napoleonit.crmlibrary.data.db.UXRocketDataBase
import com.napoleonit.crmlibrary.data.db.dao.UXRocketDao
import com.napoleonit.crmlibrary.data.models.SaveAppParamBuilder
import com.napoleonit.crmlibrary.data.models.SaveRawAppParamsRequestModel
import com.napoleonit.crmlibrary.data.models.UXRocketMetaDataEntity
import com.napoleonit.crmlibrary.data.network.BaseUXRocketApiException
import com.napoleonit.crmlibrary.data.useCases.InitUseCase
import com.napoleonit.crmlibrary.data.useCases.SaveAppParamsUseCase
import com.napoleonit.crmlibrary.di.DI
import com.napoleonit.crmlibrary.shared.CRMConstants.DEVICE_TYPE
import com.napoleonit.crmlibrary.shared.CRMConstants.NAME_SDK
import com.napoleonit.crmlibrary.shared.CRMConstants.OS_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.*


object UXRocket {
    @SuppressLint("HardwareIds")
    fun init(appContext: Context, authKey: String, deviceModelName: String, appRocketId: String) {
        DI.init(appContext)
        try {
            with(appContext) {
                val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
                val versionName = packageInfo.versionName
                val deviceID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                val osVersion = Build.VERSION.SDK_INT.toString()
                val local = Locale.getDefault().language
                val cachingUXRocketMetaDataEntity = UXRocketMetaDataEntity(
                    authKey = authKey,
                    appPackageName = packageName,
                    appVersionName = versionName,
                    deviceID = deviceID,
                    osVersion = osVersion,
                    deviceLocale = local,
                    osName = OS_NAME,
                    deviceType = DEVICE_TYPE,
                    appRocketId = appRocketId,
                    deviceModelName = deviceModelName
                )

                val useCase: InitUseCase by inject(InitUseCase::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    useCase.run(cachingUXRocketMetaDataEntity)
                    Log.i(NAME_SDK, "Initialize completed")
                }
            }
        } catch (e: Exception) {
            Log.e(NAME_SDK, e.message ?: "Initialize $NAME_SDK Error")
        }
    }

    fun saveAppParams(builder: SaveAppParamBuilder) {
        val useCase: SaveAppParamsUseCase by inject(SaveAppParamsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            useCase(
                params = builder,
                onSuccess = {
                    Log.i(NAME_SDK, "Params saved")
                },
                onFailure = {
                    if (it is BaseUXRocketApiException) when (it) {
                        BaseUXRocketApiException.ApiKeyNotFound -> Log.e(NAME_SDK, "Save app Params failed: ${it.message}")
                        BaseUXRocketApiException.FailedToSaveQueue -> Log.e(NAME_SDK, "Save app Params failed: ${it.message}")
                        BaseUXRocketApiException.Unauthorized -> Log.e(NAME_SDK, "Save app Params failed: ${it.message}")
                    }
                }
            )
        }
    }
}