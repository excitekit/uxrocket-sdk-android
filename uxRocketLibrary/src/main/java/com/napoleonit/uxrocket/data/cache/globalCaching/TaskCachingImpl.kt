package com.napoleonit.uxrocket.data.cache.globalCaching

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@SuppressLint("CommitPrefEdits")
class TaskCachingImpl(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) : ITaskCaching {
    companion object {
        const val LOG_EVENT_TASK_LIST = "LogEventTaskList"
    }

    override val logEventTaskList: List<LogModel>
        get() {
            val dataJson = sharedPreferences.getString(LOG_EVENT_TASK_LIST, "")
            return if (dataJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeFromString(ListSerializer(LogModel.serializer()), dataJson)
            }
        }

    override fun addLogEventTaskToQueue(logModel: LogModel) {
        val modifiedList = ArrayList(logEventTaskList)
        modifiedList.add(logModel)
        val dataJson = json.encodeToString(ListSerializer(LogModel.serializer()), modifiedList)
        sharedPreferences.edit().putString(LOG_EVENT_TASK_LIST, dataJson).apply()

        "Log Cached.".logInfo()
    }

    override fun removeLogEventTaskList() {
        "Logs removed from cache.".logInfo()
        sharedPreferences.edit().remove(LOG_EVENT_TASK_LIST).apply()
    }
}