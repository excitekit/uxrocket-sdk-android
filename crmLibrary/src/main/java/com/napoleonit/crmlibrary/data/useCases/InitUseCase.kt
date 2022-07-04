package com.napoleonit.crmlibrary.data.useCases

import com.napoleonit.crmlibrary.data.base.Either
import com.napoleonit.crmlibrary.data.base.Failure
import com.napoleonit.crmlibrary.data.base.Success
import com.napoleonit.crmlibrary.data.base.UseCase
import com.napoleonit.crmlibrary.data.models.UXRocketMetaDataEntity
import com.napoleonit.crmlibrary.data.repository.IUXRocketRepository

class InitUseCase(private val repository: IUXRocketRepository) : UseCase<Unit, UXRocketMetaDataEntity>() {
    override suspend fun run(params: UXRocketMetaDataEntity): Either<Exception, Unit> {
        return try {
            repository.cacheMetaData(params)
            Success(Unit)
        } catch (e: Exception) {
            Failure(e)
        }
    }
}