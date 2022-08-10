package com.napoleonit.uxrocket.data.cache.globalCaching

import com.napoleonit.uxrocket.data.models.local.ElementModel
import com.napoleonit.uxrocket.data.models.local.LogCampaignModel
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.models.local.ParentElementModel

/**
 * Интерфейс для кэширования
 */
interface ICaching {
    /**
     * SDK
     **/
    val installEventCalled: Boolean
    fun setInstallEvent(isCalled: Boolean)

    /**
     * LogEvent
     **/
    val logEventTaskList: List<LogModel>
    fun addLogEventTaskToQueue(logModel: LogModel)
    fun removeLogEventTaskList()

    /**
     * LogCampaignEvent
     **/
    val logCampaignEventTaskList: List<LogCampaignModel>
    fun addLogCampaignEventTaskToQueue(logCampaignModel: LogCampaignModel)
    fun removeLogCampaignEventTaskList()

    /**
     * Variant's element
     **/
    fun getElementsByActivityOrFragmentName(activityOrFragmentName: String): ParentElementModel?
    fun getAllElements(): List<ParentElementModel>?
    fun cacheElements(needCacheElements: List<ElementModel>, activityOrFragmentName: String)
}