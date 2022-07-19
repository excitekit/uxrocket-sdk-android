package com.napoleonit.uxrocket.data.exceptions

sealed class BaseUXRocketApiException(message: String) : Exception(message) {
    companion object {
        const val API_KEY_NOT_FOUND = 400
        const val UNAUTHORIZED = 401
        const val FAILED_TO_SAVE_DATA_TO_QUEUE = 500
    }

    object ApiKeyNotFound : BaseUXRocketApiException("API key not found")
    object Unauthorized : BaseUXRocketApiException("Unauthorized")
    object FailedToSaveQueue : BaseUXRocketApiException("Failed to save queue")
}


class UXRocketNotInitializedException :
    Exception("UXRocketNotInitializedException: The object has not been initialized. First you need to call the init method")