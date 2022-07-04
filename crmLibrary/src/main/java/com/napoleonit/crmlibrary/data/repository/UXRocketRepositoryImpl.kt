package com.napoleonit.crmlibrary.data.repository

import com.napoleonit.crmlibrary.data.api.UXRocketApi
import com.napoleonit.crmlibrary.data.db.dao.UXRocketDao
import com.napoleonit.crmlibrary.data.models.SaveRawAppParamsRequestModel
import com.napoleonit.crmlibrary.data.models.UXRocketMetaDataEntity

class UXRocketRepositoryImpl(private val api: UXRocketApi, private val dao: UXRocketDao) : IUXRocketRepository {
    override suspend fun cacheMetaData(model: UXRocketMetaDataEntity) {
        dao.insert(model)
    }

    override suspend fun getMetaData(): UXRocketMetaDataEntity {
        return dao.getEntity().first()
    }

    override suspend fun saveAppRawData(model: SaveRawAppParamsRequestModel) {
        return api.saveAppRawData(model)
    }

}