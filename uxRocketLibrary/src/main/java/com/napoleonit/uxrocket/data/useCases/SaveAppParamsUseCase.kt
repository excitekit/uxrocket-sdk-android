package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ITaskCaching
import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class SaveAppParamsUseCase(
    private val repository: IUXRocketRepository,
    private val taskCaching: ITaskCaching,
    private val metaInfo: IMetaInfo
) : UseCase<Unit, LogModel>() {

    private var sendFromCacheProcessRunning: Boolean = false

    override suspend fun run(params: LogModel): Either<Exception, Unit> {
        if (!sendFromCacheProcessRunning) sendFromCache()
        return try {
            val requestBody = SaveRawAppParamsRequestModel.bindModel(model = params, metaInfo = metaInfo)
            Success(repository.saveAppRawData(model = requestBody))
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
                val requestBody = SaveRawAppParamsRequestModel.bindModel(model = logModel, metaInfo = metaInfo)
                try {
                    repository.saveAppRawData(model = requestBody)
                } catch (e: Exception) {
                    "Send SaveRawAppData request from cache failure".logError()
                    taskCaching.addLogEventTaskToQueue(logModel)
                }
            }
        }

        sendFromCacheProcessRunning = false
    }
}