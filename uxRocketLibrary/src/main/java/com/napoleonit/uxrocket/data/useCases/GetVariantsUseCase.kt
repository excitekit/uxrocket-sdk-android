package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.local.GetVariantsModel
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.serialization.json.Json

class GetVariantsUseCase(
    private val paramsRepository: IParamsRepository,
    private val repository: IUXRocketRepository,
    private val metaInfo: IMetaInfo,
    private val caching: ICaching
) : UseCase<List<Campaign>, GetVariantsModel>() {
    override suspend fun run(params: GetVariantsModel): Either<Exception, List<Campaign>> {
        return try {
            val elements = caching.getElementsByActivityOrFragmentName(params.activityOrFragmentName)

            //Если разработчик передал пустой список параметров,
            // то мы берем список из кэша (если были ранее сохраннены)
            if (params.parameters.isNullOrEmpty()) {
                params.parameters = paramsRepository.getParams()
            }

            val requestModel = GetVariantsRequestModel.bindRequestModel(params.activityOrFragmentName, metaInfo, params.parameters, elements)

            //Todo удалить при релизе
            Json.encodeToString(GetVariantsRequestModel.serializer(), requestModel).logInfo()

            val response = repository.getVariants(requestModel)
            Success(response)

        } catch (e: Exception) {
            Failure(e)
        }
    }
}


