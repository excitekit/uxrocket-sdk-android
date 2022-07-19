package com.napoleonit.uxrocket.data.repository.uxRocketRepository

import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel

interface IUXRocketRepository {
    suspend fun saveAppRawData(model: SaveRawAppParamsRequestModel)
}