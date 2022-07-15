package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel
import com.napoleonit.uxrocket.data.models.local.LogModel
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.data.sessionCaching.IMetaInfo

class SaveAppParamsUseCase(private val repository: IUXRocketRepository, private val metaInfo: IMetaInfo) : UseCase<Unit, LogModel>() {
    override suspend fun run(params: LogModel): Either<Exception, Unit> {
        return try {
            val requestBody = SaveRawAppParamsRequestModel.bindModel(model = params, metaInfo = metaInfo)
            Success(repository.saveAppRawData(model = requestBody))
        } catch (e: Exception) {
            Failure(e)
        }
    }
}