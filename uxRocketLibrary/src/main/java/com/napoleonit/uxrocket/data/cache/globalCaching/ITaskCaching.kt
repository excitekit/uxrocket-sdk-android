package com.napoleonit.uxrocket.data.cache.globalCaching

import com.napoleonit.uxrocket.data.models.local.LogModel

/**
 * Интерфейс для кэширования Task-ов
 */
interface ITaskCaching {
    val logEventTaskList: List<LogModel>
    fun addLogEventTaskToQueue(logModel: LogModel)
    fun removeLogEventTaskList()
}