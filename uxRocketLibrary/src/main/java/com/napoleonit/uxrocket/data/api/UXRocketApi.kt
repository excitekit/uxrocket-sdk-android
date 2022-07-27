package com.napoleonit.uxrocket.data.api

import com.napoleonit.uxrocket.data.models.http.Campaign
import com.napoleonit.uxrocket.data.models.http.GetVariantsRequestModel
import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UXRocketApi {
    @PUT("mobile/SaveRawAppData")
    suspend fun saveAppRawData(@Body model: SaveRawAppParamsRequestModel)

    @POST("mobile/GetVariants")
    suspend fun getVariants(@Body model: GetVariantsRequestModel): List<Campaign>
}