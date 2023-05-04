package com.napoleonit.uxrocket.data.api

import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveIdentityMatchModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppCampaignDataRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppDataRequestModel

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UXRocketApi {
    @PUT("mobile/SaveRawAppData")
    suspend fun saveAppRawData(@Body model: SaveRawAppDataRequestModel)

    @PUT("mobile/SaveProfileIdentityMatch")
    suspend fun saveProfileIdentityMatch(@Body model: SaveIdentityMatchModel)

    @PUT("mobile/SaveRawAppCampaignData")
    suspend fun saveRawAppCampaignData(@Body model: SaveRawAppCampaignDataRequestModel)

    @POST("mobile/GetVariants")
    suspend fun getVariants(@Body model: GetVariantsRequestModel): List<Campaign>
}