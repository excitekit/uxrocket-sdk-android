package com.napoleonit.uxrocket.data.repository.uxRocketRepository

import com.napoleonit.uxrocket.data.api.UXRocketApi
import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveIdentityMatchModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppCampaignDataRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppDataRequestModel

class UXRocketRepositoryImpl(
    private val api: UXRocketApi
    ) : IUXRocketRepository {
    override suspend fun saveRawAppData(model: SaveRawAppDataRequestModel) {
        return api.saveAppRawData(model)
    }

    override suspend fun saveIdentityMath(model: SaveIdentityMatchModel) {
        return api.saveProfileIdentityMatch(model)
    }

    override suspend fun getVariants(model: GetVariantsRequestModel): List<Campaign> {
        return api.getVariants(model)
    }

    override suspend fun saveRawAppCampaignData(model: SaveRawAppCampaignDataRequestModel) {
        return api.saveRawAppCampaignData(model)
    }
}