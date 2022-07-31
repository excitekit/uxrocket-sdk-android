package com.napoleonit.uxrocket

import android.content.Context
import android.view.View
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.local.GetVariantsModel
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetVariantsUseCase
import com.napoleonit.uxrocket.data.useCases.SaveRawAppDataUseCase
import com.napoleonit.uxrocket.di.DI
import com.napoleonit.uxrocket.shared.*
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.resume

object UXRocket {

    var isDebugModeEnabled = false
        private set

    /**
     * Метод для вкл/выкл логирования событии SDK
     */
    fun debugMode(isEnabled: Boolean) {
        isDebugModeEnabled = isEnabled
    }

    /**
     * Значения для проверки SDK инициализированный или нет
     */
    var isInitialized: Boolean = false

    /**
     * Метод для инитиализации SDK
     */
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

        val taskCaching: ICaching by inject(ICaching::class.java)

        //Логика в том что при вызове метода configure (инитиализации SDK) мы логируем событие INSTALL автономно
        if (!taskCaching.installEventCalled)
            logEvent(
                itemIdentificator = "",
                itemName = "UXRocket installed",
                event = ContextEvent.INSTALL
            )
    }


    /**
     * Вызывает метод SaveRawAppData (аналог названия LogEvent)
     **/
    fun logEvent(
        itemIdentificator: String,
        itemName: String,
        event: ContextEvent,
        parameters: List<AttributeParameter>? = null
    ) {
        val saveRawAppDataUseCase: SaveRawAppDataUseCase by inject(SaveRawAppDataUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val logModel = LogModel(itemIdentificator, itemName, event, parameters ?: getAttributeParametersFromCache(this))
            saveRawAppDataUseCase(
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
                            val taskCaching by inject<ICaching>(ICaching::class.java)
                            taskCaching.addLogEventTaskToQueue(logModel)
                        }
                    }

                }
            )
        }
    }

    /**
     * Данный метод вызывает метод GetVariant's
     **/
    fun getUIConfiguration(forItem: String, parameters: List<AttributeParameter>? = null, callback: (List<Campaign>) -> Unit) {
        val getVariantsUseCase: GetVariantsUseCase by inject(GetVariantsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val getVariantModel = GetVariantsModel(
                forItem = forItem,
                parameters = parameters ?: getAttributeParametersFromCache(this)
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

    /**
     * Данный метод возврощает список #AttributeParameter из кэша
     **/
    private suspend fun getAttributeParametersFromCache(coroutineScope: CoroutineScope) =
        suspendCancellableCoroutine<List<AttributeParameter>?> { suspendCancellable ->

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

    /**
     * Данный метод сохраняет поля country и city для запросов LogEvent
     */
    fun setCountryAndCity(country: String, city: String) {
        val metaInfo: IMetaInfo by inject(IMetaInfo::class.java)
        metaInfo.setCountryAndCity(country = country, city = city)
    }

    /**
     * Данный метод сохраняет параметры по умолчанию в сесию (Кэширует), и при каждом вызове метода запроса
     * в поле 'params' он будет вставлен автоматически если разработчик ручно не ввел другие обьекты массива в поле 'params'
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

    /**
     * Данный метод кастомизирует UI элементы (Button, ImageView, EditText, TextView..)
     *
     * PS: campaigns элементы разработчик МП получает при вызове метода GetVariant's
     **/
    fun customizeItems(items: List<View>, campaigns: List<Campaign>) {
        campaigns.forEach { campaign ->
            campaign.variants.forEach { variant ->
                variant.variantAttrs?.forEach { variantAttr ->
                    findAndGetViewInItemsById(id = variantAttr.item, items = items)?.customize(variantAttr.attributes)
                }
            }
        }
    }
}