package com.napoleonit.uxrocket

import android.content.Context
import com.napoleonit.uxrocket.data.cache.globalCaching.ITaskCaching
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.local.GetVariantsModel
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetVariantsUseCase
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsUseCase
import com.napoleonit.uxrocket.di.DI
import com.napoleonit.uxrocket.shared.UXRocketServer
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.resume

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
        appRocketId: String,
        serverEnvironment: UXRocketServer
    ) {
        DI.configure(
            appContext = appContext,
            authKey = authKey,
            appRocketId = appRocketId,
            serverEnvironment = serverEnvironment
        )
        isInitialized = true
    }

    fun logEvent(
        itemIdentificator: String,
        itemName: String,
        event: ContextEvent,
        parameters: List<AttributeParameter>? = null
    ) {
        val saveAppParamsUseCase: SaveAppParamsUseCase by inject(SaveAppParamsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val logModel = LogModel(itemIdentificator, itemName, event, parameters ?: getFromCache(this))
            saveAppParamsUseCase(
                params = logModel,
                onSuccess = {
                    "Params saved".logInfo()
                },
                onFailure = {
                    "Save app Params failed: ${it.message}".logError()

                    when (it) {
                        // Проверяем причину сбоя, если причина один из нижних приведенных exception-ов
                        // кэшируем запрос
                        BaseUXRocketApiException.FailedToSaveQueue,
                        BaseUXRocketApiException.NoInternetConnection -> {
                            val taskCaching by inject<ITaskCaching>(ITaskCaching::class.java)
                            taskCaching.addLogEventTaskToQueue(logModel)
                        }
                    }

                }
            )
        }
    }

    fun getUIConfiguration(forItem: String, parameters: List<AttributeParameter>? = null, callback: (List<Campaign>) -> Unit) {
        val getVariantsUseCase: GetVariantsUseCase by inject(GetVariantsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val getVariantModel = GetVariantsModel(
                forItem = forItem,
                parameters = parameters ?: getFromCache(this)
            )
            getVariantsUseCase(
                params = getVariantModel,
                onSuccess = { campaigns ->
                    callback.invoke(campaigns)
                },
                onFailure = {
                    "Get variant's failed: ${it.message}".logError()
                })
        }
    }

    private suspend fun getFromCache(coroutineScope: CoroutineScope) = suspendCancellableCoroutine<List<AttributeParameter>?> { suspendCancellable ->
        val useCase: GetParamsUseCase by inject(GetParamsUseCase::class.java)
        coroutineScope.launch {
            useCase(
                params = Unit,
                onSuccess = { parameters ->
                    suspendCancellable.resume(parameters)
                },
                onFailure = {
                    suspendCancellable.cancel(null)
                }
            )
        }
    }

    fun setCountryAndCity(country: String, city: String) {
        val metaInfo: IMetaInfo by inject(IMetaInfo::class.java)
        metaInfo.setCountryAndCity(country = country, city = city)
    }

    /**
     * Данный метод сохраняет параметры по умолчанию в сесию (Кэширует), и при каждом вызове метода запроса
     * в поле 'params' он будет вставлена автоматически если разработчик вручно не ввел другие обьекты массива в поле 'params'
     */
    fun setDefaultsParams(params: List<AttributeParameter>) {
        val useCase: CachingParamsUseCase by inject(CachingParamsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            useCase(
                params = params,
                onSuccess = {
                    "Params saved".logInfo()
                }
            )
        }
    }
}