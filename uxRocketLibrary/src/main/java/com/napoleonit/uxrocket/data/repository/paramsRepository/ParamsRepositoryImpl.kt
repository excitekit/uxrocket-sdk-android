package com.napoleonit.uxrocket.data.repository.paramsRepository

import com.napoleonit.uxrocket.data.models.http.AttributeParameter


class ParamsRepositoryImpl : IParamsRepository {
    private val paramsData = ArrayList<AttributeParameter>()

    override fun addParams(params: List<AttributeParameter>) {
        paramsData.addAll(params)
    }

    override fun getParams(): List<AttributeParameter> {
        return paramsData.toList()
    }

    override fun removeParams(params: List<AttributeParameter>) {
        paramsData.clear()
    }
}