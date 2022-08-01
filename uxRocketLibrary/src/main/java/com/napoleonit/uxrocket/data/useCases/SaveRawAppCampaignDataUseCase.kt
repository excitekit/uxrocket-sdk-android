package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.SaveRawAppCampaignDataRequestModel
import com.napoleonit.uxrocket.data.models.local.LogCampaignModel
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.serialization.json.Json

class SaveRawAppCampaignDataUseCase(
    private val paramsRepository: IParamsRepository,
    private val repository: IUXRocketRepository,
    private val caching: ICaching,
    private val metaInfo: IMetaInfo,
) : UseCase<Unit, LogCampaignModel>() {

    private var sendFromCacheProcessRunning: Boolean = false

    override suspend fun run(params: LogCampaignModel): Either<Exception, Unit> {
        if (!sendFromCacheProcessRunning) sendFromCache()
        return try {

            //Если разработчик передал пустой список параметров,
            // то мы берем список из кэша (если были ранее сохраннены)
            if (params.parameters.isNullOrEmpty()) {
                params.parameters = paramsRepository.getParams()
            }

            val requestBody = SaveRawAppCampaignDataRequestModel.bindRequestModel(params, metaInfo)

            Json.encodeToString(SaveRawAppCampaignDataRequestModel.serializer(), requestBody).logInfo()

            Success(repository.saveRawAppCampaignData(requestBody))
        } catch (e: Exception) {
            Failure(e)
        }
    }


    private suspend fun sendFromCache() {
        sendFromCacheProcessRunning = true

        val cachingData = caching.logCampaignEventTaskList
        caching.removeLogEventTaskList()

        if (cachingData.isEmpty()) {
            "Cached SaveRowAppData empty".logInfo()
        } else {
            cachingData.forEach { logCampaignModel ->
                val requestBody = SaveRawAppCampaignDataRequestModel.bindRequestModel(model = logCampaignModel, metaInfo = metaInfo)
                try {
                    repository.saveRawAppCampaignData(model = requestBody)
                } catch (e: Exception) {
                    "Send SaveRawAppData request from cache failure".logError()
                    caching.addLogCampaignEventTaskToQueue(logCampaignModel)
                }
            }
        }

        sendFromCacheProcessRunning = false
    }

}