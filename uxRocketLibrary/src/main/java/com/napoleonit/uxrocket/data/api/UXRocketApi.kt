package com.napoleonit.uxrocket.data.api

import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel

import retrofit2.http.Body
import retrofit2.http.PUT

interface UXRocketApi {
    @PUT("mobile/SaveRawAppData")
    suspend fun saveAppRawData(@Body model: SaveRawAppParamsRequestModel)
}