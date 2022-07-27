package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.local.GetVariantsModel
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository

class GetVariantsUseCase(
    private val repository: IUXRocketRepository,
    private val metaInfo: IMetaInfo
) : UseCase<List<Campaign>, GetVariantsModel>() {
    override suspend fun run(params: GetVariantsModel): Either<Exception, List<Campaign>> {
        return try {
            val requestModel = GetVariantsRequestModel.bindRequestModel(params.forItem, metaInfo, params.parameters)
            Success(repository.getVariants(requestModel))
        } catch (e: Exception) {
            Failure(e)
        }
    }
}