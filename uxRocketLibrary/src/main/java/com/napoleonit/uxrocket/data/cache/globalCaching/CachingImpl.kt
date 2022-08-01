package com.napoleonit.uxrocket.data.cache.globalCaching

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.napoleonit.uxrocket.data.models.local.ParentElementModel
import com.napoleonit.uxrocket.data.models.local.ElementModel
import com.napoleonit.uxrocket.data.models.local.LogCampaignModel
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@SuppressLint("CommitPrefEdits")
class CachingImpl(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) : ICaching {

    companion object {
        const val LOG_EVENT_TASK_LIST = "LogEventTaskList"
        const val LOG_CAMPAIGN_EVENT_TASK_LIST = "LogCampaignEventTaskList"
        const val VARIANTS_ELEMENT_LIST = "VariantsElementList"
        const val INSTALL_EVENT_CALLED = "InstallEventCalled"
    }

    override val logEventTaskList: List<LogModel>
        @Synchronized get() {
            val dataJson = sharedPreferences.getString(LOG_EVENT_TASK_LIST, "")
            return if (dataJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeFromString(ListSerializer(LogModel.serializer()), dataJson)
            }
        }

    override val logCampaignEventTaskList: List<LogCampaignModel>
        @Synchronized get() {
            val dataJson = sharedPreferences.getString(LOG_CAMPAIGN_EVENT_TASK_LIST, "")
            return if (dataJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeFromString(ListSerializer(LogCampaignModel.serializer()), dataJson)
            }
        }


    override val installEventCalled: Boolean
        get() = sharedPreferences.getBoolean(INSTALL_EVENT_CALLED, false)

    override fun setInstallEvent(isCalled: Boolean) {
        sharedPreferences.edit().putBoolean(INSTALL_EVENT_CALLED, isCalled).apply()
    }


    override fun addLogEventTaskToQueue(logModel: LogModel) {
        val modifiedList = ArrayList(logEventTaskList)
        modifiedList.add(logModel)
        val dataJson = json.encodeToString(ListSerializer(LogModel.serializer()), modifiedList)
        sharedPreferences.edit().putString(LOG_EVENT_TASK_LIST, dataJson).apply()

        "Log cached.".logInfo()
    }

    override fun removeLogEventTaskList() {
        "Logs removed from cache.".logInfo()
        sharedPreferences.edit().remove(LOG_EVENT_TASK_LIST).apply()
    }

    override fun addLogCampaignEventTaskToQueue(logCampaignModel: LogCampaignModel) {
        val modifiedList = ArrayList(logCampaignEventTaskList)
        modifiedList.add(logCampaignModel)
        val dataJson = json.encodeToString(ListSerializer(LogCampaignModel.serializer()), modifiedList)
        sharedPreferences.edit().putString(LOG_EVENT_TASK_LIST, dataJson).apply()

        "Campaign log cached.".logInfo()
    }

    override fun removeLogCampaignEventTaskList() {
        "Campaign logs removed from cache.".logInfo()
        sharedPreferences.edit().remove(LOG_CAMPAIGN_EVENT_TASK_LIST).apply()
    }

    override fun getElementByActivityOrFragmentName(activityOrFragmentName: String): ParentElementModel? {
        return getElements().find { it.activityOrFragmentName == activityOrFragmentName }
    }

    override fun getElements(): List<ParentElementModel> {
        val dataJson = sharedPreferences.getString(VARIANTS_ELEMENT_LIST, "")

        return if (dataJson.isNullOrEmpty()) emptyList()
        else json.decodeFromString(ListSerializer(ParentElementModel.serializer()), dataJson)
    }

    override fun addElements(activityOrFragmentName: String, elements: List<ElementModel>) {
        val modifiedList = ArrayList(getElements())

        if (modifiedList.isElementExist(activityOrFragmentName)) {
            modifiedList[modifiedList.getElementIndex(activityOrFragmentName)] = ParentElementModel(activityOrFragmentName, elements)
        } else {
            modifiedList.add(ParentElementModel(activityOrFragmentName, elements))
        }

        val dataJson = json.encodeToString(ListSerializer(ParentElementModel.serializer()), modifiedList)
        sharedPreferences.edit().putString(VARIANTS_ELEMENT_LIST, dataJson).apply()
    }

    private fun List<ParentElementModel>.isElementExist(ActivityOrFragmentName: String): Boolean {
        return find { it.activityOrFragmentName == ActivityOrFragmentName } != null
    }

    private fun List<ParentElementModel>.getElementIndex(ActivityOrFragmentName: String): Int {
        return indexOf(find { it.activityOrFragmentName == ActivityOrFragmentName })
    }
}