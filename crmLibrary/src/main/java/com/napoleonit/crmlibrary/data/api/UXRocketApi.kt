package com.napoleonit.crmlibrary.data.api

import com.napoleonit.crmlibrary.data.models.SaveRawAppParamsRequestModel

import retrofit2.http.Body
import retrofit2.http.POST

interface UXRocketApi {
    @POST("api/saverawappdata/")
    suspend fun saveAppRawData(@Body model: SaveRawAppParamsRequestModel): Unit
}