package com.napoleonit.uxrocket

import android.content.Context
import android.util.Log
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.data.models.local.Param
import com.napoleonit.uxrocket.data.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsUseCase
import com.napoleonit.uxrocket.di.DI
import com.napoleonit.uxrocket.shared.UXRocketConstants.NAME_SDK
import com.napoleonit.uxrocket.shared.execute
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

object UXRocket {

    var isDebugModeEnabled = false
        private set

    fun debugModel(isEnabled: Boolean) {
        isDebugModeEnabled = isEnabled
    }

    var isInitialized: Boolean = false
    fun configure(
        appContext: Context,
        authKey: String,
        appRocketId: String
    ) {
        DI.configure(
            appContext = appContext,
            authKey = authKey,
            appRocketId = appRocketId
        )
        isInitialized = true
    }

    fun logEvent(model: LogModel) {
        val useCase: SaveAppParamsUseCase by inject(SaveAppParamsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).execute(block = {
            useCase(
                params = model,
                onSuccess = {
                    "Params saved".logInfo()
                },
                onFailure = {
                    if (it is BaseUXRocketApiException) when (it) {
                        BaseUXRocketApiException.ApiKeyNotFound -> "Save app Params failed: ${it.message}".logError()
                        BaseUXRocketApiException.FailedToSaveQueue -> "Save app Params failed: ${it.message}".logError()
                        BaseUXRocketApiException.Unauthorized -> "Save app Params failed: ${it.message}".logError()
                    }
                }
            )
        })
    }

    fun setCountryAndCity(country: String, city: String) {
        val metaInfo: IMetaInfo by inject(IMetaInfo::class.java)
        metaInfo.setCountryAndCity(country = country, city = city)
    }

    /**
     * Данный метод назначает параметры по умолчанию в сесию (Кэширует), и при каждом вызове метода запросов
     * в поле 'params' он будет вставлена если разработчик вручно не ввел другие обьекты массива 'params'
     */
    fun setDefaultsParams(params: List<Param>) {
        val useCase: CachingParamsUseCase by inject(CachingParamsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).execute(block = {
            useCase(
                params = params,
                onSuccess = {
                    "Params saved".logInfo()
                }
            )
        })
    }
}