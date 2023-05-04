package com.napoleonit.uxrocket

import android.content.Context
import android.view.View
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.data.models.http.*
import com.napoleonit.uxrocket.data.models.local.*
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetVariantsUseCase
import com.napoleonit.uxrocket.data.useCases.SaveIdentityMatchUseCase
import com.napoleonit.uxrocket.data.useCases.SaveRawAppCampaignDataUseCase
import com.napoleonit.uxrocket.data.useCases.SaveRawAppDataUseCase
import com.napoleonit.uxrocket.di.DI
import com.napoleonit.uxrocket.shared.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

object UXRocket {

    private val allCampaignInSession = ArrayList<Campaign>()

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
        serverEnvironment: UXRocketServer,
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
        parameters: List<AttributeParameter>? = null,
        cartSum: Double? = null,
        productCount: Int? = null,
        productPrice: Double? = null,
        productCode: String? = null
    ) {
        val saveRawAppDataUseCase: SaveRawAppDataUseCase by inject(SaveRawAppDataUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val logModel = LogModel(
                item = itemIdentificator,
                itemName = itemName,
                event = event,
                parameters = parameters
            )
            logModel.cartSum = cartSum
            logModel.productCount = productCount
            logModel.productPrice = productPrice
            logModel.productCode = productCode
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
                        BaseUXRocketApiException.NoInternetConnection,
                        -> {
                            val taskCaching by inject<ICaching>(ICaching::class.java)
                            taskCaching.addLogEventTaskToQueue(logModel)
                        }
                    }

                }
            )
        }
    }

    /**
     * Вызывает метод SaveRawCampaignData (аналог названия LogCampaignEvent)
     **/
    private fun logCampaignEvent(logCampaignModel: LogCampaignModel) {
        val saveRawAppCampaignDataUseCase: SaveRawAppCampaignDataUseCase by inject(
            SaveRawAppCampaignDataUseCase::class.java
        )
        CoroutineScope(Dispatchers.IO).launch {
            saveRawAppCampaignDataUseCase(
                params = logCampaignModel,
                onSuccess = {
                    "Params saved".logInfo()
                },
                onFailure = {
                    "Save app Params failed: ${it.message}".logError()

                    when (it) {
                        // Проверяем причину сбоя, если причина один из нижних приведенных exception-ов
                        // кэшируем запрос
                        BaseUXRocketApiException.FailedToSaveQueue,
                        BaseUXRocketApiException.NoInternetConnection,
                        -> {
                            val taskCaching by inject<ICaching>(ICaching::class.java)
                            taskCaching.addLogCampaignEventTaskToQueue(logCampaignModel)
                        }
                    }

                }
            )
        }
    }

    fun logIdentityMatch(logIdentityModel: LogIdentityModel) {
        val saveIdentityMatchUseCase: SaveIdentityMatchUseCase by inject(
            SaveIdentityMatchUseCase::class.java
        )
        CoroutineScope(Dispatchers.IO).launch {
            saveIdentityMatchUseCase(
                params = logIdentityModel,
                onSuccess = {
                    "Identity saved".logInfo()
                },
                onFailure = {
                    "Save Identity failed: ${it.message}".logError()

                    when (it) {
                        BaseUXRocketApiException.FailedToSaveQueue,
                        BaseUXRocketApiException.NoInternetConnection,
                        -> {
                            val taskCaching by inject<ICaching>(ICaching::class.java)
                            taskCaching.addLogIdentityTaskToQueue(logIdentityModel)
                        }
                    }

                }
            )
        }
    }

    /**
     * Метод который вызывается автономно при успешном запросе getVariants
     **/
    private fun logCampaignOpenPageEvent(
        activityOrFragmentName: String,
        campaignId: Long,
        parameters: List<AttributeParameter>? = null,
        variants: Map<String, Long?>? = null,
    ) {

        val logCampaignModel = LogCampaignModel(
            actionName = "openpage",
            parameters = parameters,
            campaignId = campaignId,
            variants = variants,
            activityOrFragmentName = activityOrFragmentName,
            countingType = CountingType.SHOW
        )

        logCampaignEvent(logCampaignModel)
    }


    /**
     * Данный метод вызывает метод GetVariant's
     **/
    fun getUIConfiguration(
        activityOrFragmentName: String,
        parameters: List<AttributeParameter>? = null,
        callback: (List<Campaign>) -> Unit
    ) {
        val getVariantsUseCase: GetVariantsUseCase by inject(GetVariantsUseCase::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val getVariantModel = GetVariantsModel(
                activityOrFragmentName = activityOrFragmentName,
                parameters = parameters
            )
            getVariantsUseCase(
                params = getVariantModel,
                onSuccess = { campaigns ->
                    insertCampaignsInSession(campaigns)
                    campaigns.forEach { campaign ->
                        logCampaignOpenPageEvent(
                            activityOrFragmentName = activityOrFragmentName,
                            campaignId = campaign.id,
                            parameters = parameters,
                            variants = campaign.bindVariantsForRequest(),
                        )

                    }
                    callback.invoke(campaigns)
                },
                onFailure = {
                    "Get variant's failed: ${it.message}".logError()
                })
        }
    }

    private fun insertCampaignsInSession(campaigns: List<Campaign>) {
        campaigns.forEach { campaign ->
            val foundCampaign = allCampaignInSession.find { it.id == campaign.id }
            val isExist = foundCampaign != null
            if (isExist) {
                val foundIndex = allCampaignInSession.indexOf(foundCampaign)
                allCampaignInSession[foundIndex] = campaign
            } else {
                allCampaignInSession.add(campaign)
            }
        }
    }

    private fun applyCountingTypeTwoAction(
        campaigns: List<Campaign>,
        activityOrFragmentName: String,
        totalValue: Int? = null,
        parameters: List<AttributeParameter>?
    ) {
        campaigns.map { campaign ->
            campaign.actions.map { action: Action ->
                if (action.countingType == CountingType.COUNTING_PARAMETER_2) {
                    logCampaignEvent(
                        LogCampaignModel(
                            actionName = action.name,
                            activityOrFragmentName = activityOrFragmentName,
                            campaignId = campaign.id,
                            totalValue = totalValue ?: 0,
                            parameters = parameters,
                            variants = campaign.bindVariantsForRequest(),
                            countingType = CountingType.COUNTING_PARAMETER_2
                        )
                    )
                }
            }
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
    fun customizeItems(
        items: List<View>,
        campaigns: List<Campaign>,
        activityOrFragmentName: String
    ) {
        val cachingRepo: ICaching by inject(ICaching::class.java)
        val cachedElements = mutableSetOf<ElementModel>()
        campaigns.forEach { campaign ->
            campaign.variants.mapNotNull { variant ->
                variant.variantAttrs?.forEach { variantAttr ->
                    findAndGetViewInItemsById(id = variantAttr.item, items = items)?.apply {
                        customize(variantAttr.attributes)
                        cachedElements.add(
                            ElementModel(
                                id = variant.elementID,
                                campaignId = campaign.id,
                                variantId = variant.id
                            )
                        )
                    }
                }
            }
        }
        cachingRepo.cacheElements(
            activityOrFragmentName = activityOrFragmentName,
            needCacheElements = cachedElements.toList()
        )
        cachedElements.clear()
    }

    /**
     * Данный метод позволяет обрабатывать экшены, применять определенные действия к вьюхам
     */

    fun processActions(
        items: List<View>,
        campaigns: List<Campaign>,
        activityOrFragmentName: String,
        totalValue: (() -> Int?)? = null,
        parameters: List<AttributeParameter>?
    ) {
        campaigns.forEach { campaign ->
            campaign.actions.forEach { action ->
                findAndGetViewInItemsById(id = action.item, items = items)?.let { view ->
                    if (action.actionType == Action.Type.CLICK && action.countingType == CountingType.COUNTING_PARAMETER) {
                        view.setOnClickListener {
                            logEvent(
                                itemIdentificator = action.item,
                                itemName = action.name,
                                event = ContextEvent.BUTTONS
                            )
                            val logCampaignModel = LogCampaignModel(
                                activityOrFragmentName = activityOrFragmentName,
                                campaignId = campaign.id,
                                totalValue = 1,
                                actionName = action.name,
                                parameters = parameters,
                                variants = campaign.bindVariantsForRequest(),
                                countingType = CountingType.COUNTING_PARAMETER
                            )
                            logCampaignEvent(logCampaignModel)

                            applyCountingTypeTwoAction(
                                campaigns = campaigns,
                                activityOrFragmentName = activityOrFragmentName,
                                totalValue = totalValue?.invoke(),
                                parameters = parameters
                            )
                        }
                    }
                }
            }
        }
    }
}