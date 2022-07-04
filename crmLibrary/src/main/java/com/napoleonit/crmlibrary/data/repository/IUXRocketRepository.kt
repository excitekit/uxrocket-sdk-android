package com.napoleonit.crmlibrary.data.repository

import com.napoleonit.crmlibrary.data.models.SaveRawAppParamsRequestModel
import com.napoleonit.crmlibrary.data.models.UXRocketMetaDataEntity

interface IUXRocketRepository {
    suspend fun cacheMetaData(model: UXRocketMetaDataEntity)
    suspend fun getMetaData(): UXRocketMetaDataEntity
    suspend fun saveAppRawData(model: SaveRawAppParamsRequestModel)
}