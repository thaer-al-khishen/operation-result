package com.relatablecode.operationresult

/**
 * A sealed class to represent various types of application errors.
 */
sealed class AppError(override val message: String): BaseError {
    data class TimeoutError(val errorMessage: String) : AppError(errorMessage)
    data class NoInternetConnection(val errorMessage: String) : AppError(errorMessage)
    data class ServerError(val errorMessage: String) : AppError(errorMessage)
    data class NetworkError(val errorMessage: String) : AppError(errorMessage)
    data class InvalidDataError(val errorMessage: String) : AppError(errorMessage)
    data class NotFoundError(val errorMessage: String) : AppError(errorMessage)
    data class AuthenticationError(val errorMessage: String) : AppError(errorMessage)
    data class PermissionDeniedError(val errorMessage: String) : AppError(errorMessage)
    data class JsonParsingError(val errorMessage: String) : AppError(errorMessage)
    data class GenericError(val throwable: Throwable) :
        AppError(throwable.message ?: "An unknown error occurred")
}
