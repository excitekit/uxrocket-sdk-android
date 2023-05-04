package com.napoleonit.uxrocket.data.models.http

import com.napoleonit.uxrocket.data.base.Either
import com.napoleonit.uxrocket.data.base.Success
import com.napoleonit.uxrocket.data.base.UseCase
import com.napoleonit.uxrocket.data.cache.globalCaching.ICaching
import com.napoleonit.uxrocket.data.models.local.ParentElementModel

class GetElementsFromCacheUseCase(private val caching: ICaching) : UseCase<ParentElementModel?, String>() {
    override suspend fun run(params: String): Either<Exception, ParentElementModel?> {
        return Success(caching.getElementsByActivityOrFragmentName(params))
    }
}