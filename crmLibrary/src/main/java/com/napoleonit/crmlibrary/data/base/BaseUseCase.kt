package com.napoleonit.crmlibrary.data.base

import com.napoleonit.crmlibrary.data.network.BaseUXRocketApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Base class for a `coroutine` use case.
 */
abstract class UseCase<out Type, in Params> where Type : Any? {
    /**
     * Runs the actual logic of the use case.
     */
    abstract suspend fun run(params: Params): Either<Exception, Type>

    suspend operator fun invoke(
        params: Params, onSuccess: suspend (Type) -> Unit = {},
        onFailure: suspend (Exception) -> Unit = {},
    ) {
        val result = run(params)

        withContext(Dispatchers.IO) {
            result.fold(
                failed = {
                    if (it is HttpException) {
                        val okHttpResponse = it.response()
                        val exception = when (okHttpResponse?.code()) {
                            BaseUXRocketApiException.API_KEY_NOT_FOUND -> BaseUXRocketApiException.ApiKeyNotFound
                            BaseUXRocketApiException.UNAUTHORIZED -> BaseUXRocketApiException.Unauthorized
                            BaseUXRocketApiException.FAILED_TO_SAVE_DATA_TO_QUEUE -> BaseUXRocketApiException.FailedToSaveQueue
                            else -> it
                        }
                        onFailure(exception)
                    } else {
                        onFailure(it)
                    }
                },
                succeeded = { onSuccess(it) }
            )
        }
    }
}