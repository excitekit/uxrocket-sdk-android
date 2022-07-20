package com.napoleonit.uxrocket.data.base

import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.exceptions.BaseUXRocketApiException
import com.napoleonit.uxrocket.data.exceptions.UXRocketNotInitializedException
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsUseCase
import com.napoleonit.uxrocket.shared.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException

/**
 * Base class for a `coroutine` use case.
 */
abstract class UseCase<out Type, in Params> where Type : Any? {
    /**
     * Runs the actual logic of the use case.
     */
    abstract suspend fun run(params: Params): Either<Exception, Type>

    private val networkState: NetworkState by inject(NetworkState::class.java)

    suspend operator fun invoke(
        params: Params,
        onSuccess: suspend (Type) -> Unit = {},
        onFailure: suspend (Exception) -> Unit = {},
    ) {
        if (!UXRocket.isInitialized) throw UXRocketNotInitializedException()
        else {
            val result = run(params)
            withContext(Dispatchers.IO) {
                result.fold(
                    failed = {
                        if (!networkState.isOnline())
                            onFailure(BaseUXRocketApiException.NoInternetConnection)
                        else if (it is HttpException) {
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
}