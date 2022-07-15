package com.napoleonit.uxrocket.data.useCases

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.models.local.Param
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository

class CachingParamsUseCase(private val repository: IParamsRepository) : UseCase<Unit, List<Param>>() {
    override suspend fun run(params: List<Param>): Either<Exception, Unit> {
        repository.addParams(params)
        return Success(Unit)
    }
}