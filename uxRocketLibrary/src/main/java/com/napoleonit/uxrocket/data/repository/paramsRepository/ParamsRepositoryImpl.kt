package com.napoleonit.uxrocket.data.repository.paramsRepository

import com.napoleonit.uxrocket.data.models.local.Param

class ParamsRepositoryImpl : IParamsRepository {
    private val paramsData = ArrayList<Param>()

    override fun addParams(params: List<Param>) {
        paramsData.addAll(params)
    }

    override fun getParams(): List<Param> {
        return paramsData.toList()
    }

    override fun removeParams(params: List<Param>) {
        paramsData.clear()
    }

    override fun removeParam(param: Param) {
        paramsData.find { it.id == param.id }?.also { paramsData.remove(it) }
    }
}