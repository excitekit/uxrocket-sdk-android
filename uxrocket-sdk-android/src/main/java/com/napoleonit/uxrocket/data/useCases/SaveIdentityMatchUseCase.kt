package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.SaveIdentityMatchModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppCampaignDataRequestModel
import com.napoleonit.uxrocket.data.models.local.LogCampaignModel
import com.napoleonit.uxrocket.data.models.local.LogIdentityModel
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.shared.logError
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class SaveIdentityMatchUseCase(
    private val repository: IUXRocketRepository,
    private val caching: ICaching,
    private val metaInfo: IMetaInfo,
) : UseCase<Unit, LogIdentityModel>() {

    private var sendFromCacheProcessRunning: Boolean = false

    override suspend fun run(params: LogIdentityModel): Either<Exception, Unit> {
        if (!sendFromCacheProcessRunning) sendFromCache()
        return try {

            val requestBody = SaveIdentityMatchModel.bindRequestModel(params, metaInfo)

            Json.encodeToString(SaveIdentityMatchModel.serializer(), requestBody).logInfo()

            Success(repository.saveIdentityMath(requestBody))
        } catch (e: Exception) {
            var message = (e as HttpException).response()?.errorBody()?.string()
            message.logError();
            Failure(e)
        }
    }


    private suspend fun sendFromCache() {
        sendFromCacheProcessRunning = true

        val cachingData = caching.logIdentityTaskList
        caching.removeLogEventTaskList()

        if (cachingData.isEmpty()) {
            "Cached Identity empty".logInfo()
        } else {
            cachingData.forEach { identity ->
                val requestBody = SaveIdentityMatchModel.bindRequestModel(model = identity, metaInfo = metaInfo)
                try {
                    repository.saveIdentityMath(model = requestBody)
                } catch (e: Exception) {
                    "Send SaveIdentityMatch request from cache failure".logError()
                    caching.addLogIdentityTaskToQueue(identity)
                }
            }
        }

        sendFromCacheProcessRunning = false
    }

}