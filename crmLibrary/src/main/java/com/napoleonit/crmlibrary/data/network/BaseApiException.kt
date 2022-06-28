package com.napoleonit.crmlibrary.data.network

sealed class BaseCRMApiException(message: String) : Exception(message) {
    companion object {
        const val API_KEY_NOT_FOUND = 400
        const val UNAUTHORIZED = 401
        const val FAILED_TO_SAVE_DATA_TO_QUEUE = 500
    }

    object ApiKeyNotFound : BaseCRMApiException("API key not found")
    object Unauthorized : BaseCRMApiException("Unauthorized")
    object FailedToSaveQueue : BaseCRMApiException("Failed to save queue")
}