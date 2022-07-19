package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository

class GetParamsUseCase(private val repository: IParamsRepository)  : UseCase<List<AttributeParameter>,Unit>() {
    override suspend fun run(params: Unit): Either<Exception, List<AttributeParameter>> {
        return Success(repository.getParams())
    }
}