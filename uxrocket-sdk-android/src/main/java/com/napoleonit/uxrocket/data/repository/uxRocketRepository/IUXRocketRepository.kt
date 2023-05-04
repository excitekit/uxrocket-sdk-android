package com.napoleonit.uxrocket.data.repository.uxRocketRepository

import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveIdentityMatchModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppCampaignDataRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppDataRequestModel

interface IUXRocketRepository {
    suspend fun saveRawAppData(model: SaveRawAppDataRequestModel)
    suspend fun saveIdentityMath(model: SaveIdentityMatchModel)
    suspend fun saveRawAppCampaignData(model: SaveRawAppCampaignDataRequestModel)
    suspend fun getVariants(model: GetVariantsRequestModel): List<Campaign>
}