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
    fun getElementByFromItem(fromItem: String): ParentElementModel?
    fun getElements(): List<ParentElementModel>?
    fun addElements(fromItem: String, elements: List<ElementModel>)
}