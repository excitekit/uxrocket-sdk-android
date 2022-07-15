package com.napoleonit.uxrocket.data.repository.uxRocketRepository

import com.napoleonit.uxrocket.data.models.http.SaveRawAppParamsRequestModel
import com.napoleonit.uxrocket.data.models.entity.UXRocketMetaDataEntity

interface IUXRocketRepository {
    suspend fun saveAppRawData(model: SaveRawAppParamsRequestModel)
}