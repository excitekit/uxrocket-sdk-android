package com.napoleonit.uxrocket.data.repository.paramsRepository

import com.napoleonit.uxrocket.data.models.local.Param

interface IParamsRepository {
    fun addParams(params: List<Param>)
    fun getParams(): List<Param>
    fun removeParams(params: List<Param>)
    fun removeParam(param: Param)
}