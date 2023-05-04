package com.napoleonit.uxrocket.data.repository.paramsRepository

import com.napoleonit.uxrocket.data.models.http.AttributeParameter


interface IParamsRepository {
    fun addParams(params: List<AttributeParameter>)
    fun getParams(): List<AttributeParameter>
    fun removeParams(params: List<AttributeParameter>)
}