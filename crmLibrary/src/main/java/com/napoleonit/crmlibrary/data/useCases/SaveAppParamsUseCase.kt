package com.napoleonit.crmlibrary.data.useCases

import com.napoleonit.crmlibrary.data.base.Either
import com.napoleonit.crmlibrary.data.base.Failure
import com.napoleonit.crmlibrary.data.base.Success
import com.napoleonit.crmlibrary.data.base.UseCase
import com.napoleonit.crmlibrary.data.models.SaveAppParamBuilder
import com.napoleonit.crmlibrary.data.models.SaveRawAppParamsRequestModel
import com.napoleonit.crmlibrary.data.repository.IUXRocketRepository

class SaveAppParamsUseCase(private val repository: IUXRocketRepository) : UseCase<Unit, SaveAppParamBuilder>() {
    override suspend fun run(params: SaveAppParamBuilder): Either<Exception, Unit> {
        return try {
            val cachedMetaData = repository.getMetaData()
            val requestBody = SaveRawAppParamsRequestModel.bindModel(params, cachedMetaData)
            Success(repository.saveAppRawData(model = requestBody))
        } catch (e: Exception) {
            Failure(e)
        }
    }
}