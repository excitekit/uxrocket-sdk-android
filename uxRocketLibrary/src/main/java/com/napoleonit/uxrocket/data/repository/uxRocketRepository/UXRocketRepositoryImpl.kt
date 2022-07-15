package com.napoleonit.uxrocket.data.repository.uxRocketRepository

import com.napoleonit.uxrocket.data.api.UXRocketApi
import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel

class UXRocketRepositoryImpl(private val api: UXRocketApi) : IUXRocketRepository {
    override suspend fun saveAppRawData(model: SaveRawAppParamsRequestModel) {
        return api.saveAppRawData(model)
    }
}