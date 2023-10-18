package com.relatablecode.operationresult

import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Extension function to directly map any Throwable to an AppError.
 */
fun Throwable.toAppError(): AppError = handleThrowable(this)


/**
 * Transforms common exceptions into specific AppError subtypes.
 */
fun <E : BaseError> handleThrowable(
    throwable: Throwable,
    customHandler: ((Throwable) -> E)? = null
): E {
    customHandler?.let { return it(throwable) }

    // Default handling:
    return when (throwable) {
        is TimeoutException -> AppError.TimeoutError(throwable.message ?: "Request timed out")
        is UnknownHostException -> AppError.NoInternetConnection(throwable.message ?: "No internet connection")
        is kotlinx.serialization.SerializationException -> AppError.JsonParsingError(throwable.message ?: "JSON Parsing error with Kotlinx Serialization")
        is java.net.HttpRetryException -> AppError.ServerError(throwable.message ?: "Server error")
        is java.net.ProtocolException -> AppError.InvalidDataError(throwable.message ?: "Received invalid data")
        is java.io.FileNotFoundException -> AppError.NotFoundError(throwable.message ?: "File or resource not found")
        is IOException -> AppError.NetworkError(throwable.message ?: "Network error")
        is java.security.PrivilegedActionException -> AppError.PermissionDeniedError(throwable.message ?: "Permission denied")
        is SecurityException -> AppError.AuthenticationError(throwable.message ?: "Authentication error")
        else -> AppError.GenericError(throwable)
    } as E

}

//Usage:
sealed class CustomError(override val message: String) : BaseError {
    data class CustomTimeoutError(val errorMessage: String) : CustomError(errorMessage)
    // ... [other custom error classes]
    data class CustomGenericError(val throwable: Throwable) : CustomError(throwable.message ?: "An unknown custom error occurred")
}

fun customErrorHandler(throwable: Throwable): CustomError {
    return when (throwable) {
        is TimeoutException -> CustomError.CustomTimeoutError(throwable.message ?: "Custom request timed out")
        // Handle other exception types to map to custom errors
        else -> CustomError.CustomGenericError(throwable)
    }
}

fun main() {
//    val someThrowable: Throwable = TimeoutException("Request timed out!")
//    val customErrorResult = handleThrowable(someThrowable, ::customErrorHandler)
//    println(customErrorResult.message)

    val result: Result<String> = Result.failure(TimeoutException())
    val operationResult = result.toCustomOperationResult(::customErrorHandler)
    println(operationResult.errorOrNull()?.message)

//    val result: Result<String> = Result.failure(TimeoutException())
//    val operationResult = result.toOperationResult()
//    println(operationResult.errorOrNull()?.message)
}
