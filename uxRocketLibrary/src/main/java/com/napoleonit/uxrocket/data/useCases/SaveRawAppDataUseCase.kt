package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.models.http.SaveRawAppDataRequestModel
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo

class SaveRawAppDataUseCase(
    private val repository: IUXRocketRepository,
    private val taskCaching: ICaching,
    private val metaInfo: IMetaInfo
) : UseCase<Unit, LogModel>() {

    private var sendFromCacheProcessRunning: Boolean = false

    override suspend fun run(params: LogModel): Either<Exception, Unit> {
        if (!sendFromCacheProcessRunning) sendFromCache()
        return try {

            val needCacheInstallEventState = params.event == ContextEvent.INSTALL

            //Каждый раз берем тип подключении интернета т.к тип может менятся.
            params.connectionType = networkState.connectionType

            val requestBody = SaveRawAppDataRequestModel.bindRequestModel(model = params, metaInfo = metaInfo)
            val result = repository.saveRawAppData(model = requestBody)

            if (needCacheInstallEventState)
                taskCaching.setInstallEvent(true)

            Success(result)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    private suspend fun sendFromCache() {
        sendFromCacheProcessRunning = true

        val cachingData = taskCaching.logEventTaskList
        taskCaching.removeLogEventTaskList()

        if (cachingData.isEmpty()) {
            "Cached SaveRowAppData empty".logInfo()
        } else {
            cachingData.forEach { logModel ->
                val requestBody = SaveRawAppDataRequestModel.bindRequestModel(model = logModel, metaInfo = metaInfo)
                try {
                    repository.saveRawAppData(model = requestBody)
                } catch (e: Exception) {
                    "Send SaveRawAppData request from cache failure".logError()
                    taskCaching.addLogEventTaskToQueue(logModel)
                }
            }
        }

        sendFromCacheProcessRunning = false
    }
}