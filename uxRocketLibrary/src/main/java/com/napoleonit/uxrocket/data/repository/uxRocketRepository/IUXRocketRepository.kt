package com.napoleonit.uxrocket.data.repository.uxRocketRepository

import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel

interface IUXRocketRepository {
    suspend fun saveAppRawData(model: SaveRawAppParamsRequestModel)
    suspend fun getVariants(model: GetVariantsRequestModel): List<Campaign>
}