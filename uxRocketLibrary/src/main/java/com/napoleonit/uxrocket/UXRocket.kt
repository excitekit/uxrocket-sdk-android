package com.napoleonit.uxrocket

import android.content.Context
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.data.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetParamsUseCase
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsUseCase
import com.napoleonit.uxrocket.di.DI
import com.napoleonit.uxrocket.shared.execute
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.coroutines.*
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

    fun logEvent(
        itemIdentificator: String,
        itemName: String,
        event: ContextEvent,
        parameters: List<AttributeParameter>? = null
    ) {
        val useCase: SaveAppParamsUseCase by inject(SaveAppParamsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).execute(block = {
            useCase(
                params = LogModel(
                    item = itemIdentificator,
                    itemName = itemName,
                    event = event,
                    params = parameters ?: getFromCache(this)
                ),
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

    @ExperimentalCoroutinesApi
    private suspend fun getFromCache(coroutineScope: CoroutineScope) = suspendCancellableCoroutine<List<AttributeParameter>?> { suspendCancellable ->
        val useCase: GetParamsUseCase by inject(GetParamsUseCase::class.java)
        coroutineScope.launch {
            useCase(
                params = Unit,
                onSuccess = { suspendCancellable.resume(it) {} },
                onFailure = { suspendCancellable.cancel(null) })
        }
    }

    fun setCountryAndCity(country: String, city: String) {
        val metaInfo: IMetaInfo by inject(IMetaInfo::class.java)
        metaInfo.setCountryAndCity(country = country, city = city)
    }

    /**
     * Данный метод назначает параметры по умолчанию в сесию (Кэширует), и при каждом вызове метода запросов
     * в поле 'params' он будет вставлена если разработчик вручно не ввел другие обьекты массива 'params'
     */
    fun setDefaultsParams(params: List<AttributeParameter>) {
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