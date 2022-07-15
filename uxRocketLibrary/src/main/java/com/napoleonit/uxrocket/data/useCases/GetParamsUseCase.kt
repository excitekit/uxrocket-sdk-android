package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.models.local.Param
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository

class GetParamsUseCase(private val repository: IParamsRepository)  : UseCase<List<Param>,Unit>() {
    override suspend fun run(params: Unit): Either<Exception, List<Param>> {
        return Success(repository.getParams())
    }
}