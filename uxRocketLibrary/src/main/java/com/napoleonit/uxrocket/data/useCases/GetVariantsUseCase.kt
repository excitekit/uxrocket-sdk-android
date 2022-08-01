package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Failure
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.local.ElementModel
import com.napoleonit.uxrocket.data.models.local.GetVariantsModel
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.shared.ReadAssetsUtil
import com.napoleonit.uxrocket.shared.logInfo
import kotlinx.serialization.json.Json

class GetVariantsUseCase(
    private val repository: IUXRocketRepository,
    private val metaInfo: IMetaInfo,
    private val caching: ICaching,
    private val readAssetsUtil: ReadAssetsUtil//Todo удалить при релизе
) : UseCase<List<Campaign>, GetVariantsModel>() {
    override suspend fun run(params: GetVariantsModel): Either<Exception, List<Campaign>> {
        return try {
            val elements = caching.getElementByFromItem(params.forItem)
            val requestModel = GetVariantsRequestModel.bindRequestModel(params.forItem, metaInfo, params.parameters, elements)

            //Todo удалить при релизе
            Json.encodeToString(GetVariantsRequestModel.serializer(), requestModel).logInfo()

            val response = repository.getVariants(requestModel)
            cacheElements(response, forItem = params.forItem)
            Success(response)

            //Todo удалить при релизе
            //val tempResponse = Json.decodeFromString(ListSerializer(Campaign.serializer()), readAssetsUtil.getJsonDataFromAsset("temp.json") ?: "")
            //Success(tempResponse)
        } catch (e: Exception) {
            Failure(e)
        }
    }


    private fun cacheElements(data: List<Campaign>, forItem: String) {
        val cachedElements = ArrayList<ElementModel>()
        data.forEach { campaign ->
            campaign.variants.forEach { variant ->
                val elementModel = ElementModel(variant.elementID , campaign.id, variant.id)
                cachedElements.add(elementModel)
            }
            caching.addElements(forItem, cachedElements)
            cachedElements.clear()
        }
    }
}


