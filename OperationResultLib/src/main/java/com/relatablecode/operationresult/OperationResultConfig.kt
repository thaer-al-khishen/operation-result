package com.relatablecode.operationresult

/**
 * Singleton configuration object for Operation Results that allows for global error handling settings.
 * This should be initialized in your Application class' `onCreate` method before any operations are performed.
 *
 * Example initialization in Application.onCreate:
 * ```
 * OperationResultConfig.initialize().withBaseError { throwable ->
 *     // Map the Throwable to your custom BaseError here
 * }
 * ```
 */
object OperationResultConfig {

    /** Indicates whether the OperationResultConfig has been initialized to prevent re-initialization. */
    var isInitialized = false
        private set

    /** A handler function that converts a Throwable into a BaseError instance. */
    private var _errorHandler: ((Throwable) -> BaseError)? = null

    /**
     * Initializes the configuration. This should only be called once,
     * typically during the Application start-up.
     *
     * @throws IllegalStateException if called more than once.
     * @return The OperationResultConfig instance for chaining configuration calls.
     */
    fun initialize() = this.apply {
        if (isInitialized) {
            throw IllegalStateException("Operation Result is already initialized")
        } else {
            isInitialized = !isInitialized
        }
    }

    /**
     * Sets a custom error handling function that maps a Throwable to a BaseError.
     * This should be set after initialization and before any operations are performed.
     *
     * @param errorHandler The handler function that converts a Throwable into a BaseError.
     * @throws IllegalStateException if called before the object is initialized.
     */
    fun withBaseError(errorHandler: (Throwable) -> BaseError) {
        if (isInitialized) {
            _errorHandler = errorHandler
        } else throw IllegalStateException("Operation Result is not initialized")
    }

    /**
     * Converts a standard Kotlin Result into an OperationResult, applying the global error handler if available.
     *
     * @param result The Result object to convert.
     * @return An OperationResult object encapsulating the success or failure result.
     */
    fun <T> toOperationResult(
        result: Result<T>,
    ): OperationResult<BaseError, T> {
        return result.toCustomOperationResult(_errorHandler)
    }

}

//fun main() {
//
//    OperationResultConfig.initialize().withBaseError { throwable ->
//        when (throwable) {
//            is TimeoutException -> AppError.TimeoutError(throwable.message ?: "Request timed out from object")
//            is UnknownHostException -> AppError.NoInternetConnection(throwable.message ?: "No internet connection")
//            is kotlinx.serialization.SerializationException -> AppError.JsonParsingError(throwable.message ?: "JSON Parsing error with Kotlinx Serialization")
//            is java.net.HttpRetryException -> AppError.ServerError(throwable.message ?: "Server error")
//            is java.net.ProtocolException -> AppError.InvalidDataError(throwable.message ?: "Received invalid data")
//            is java.io.FileNotFoundException -> AppError.NotFoundError(throwable.message ?: "File or resource not found")
//            is IOException -> AppError.NetworkError(throwable.message ?: "Network error")
//            is java.security.PrivilegedActionException -> AppError.PermissionDeniedError(throwable.message ?: "Permission denied")
//            is SecurityException -> AppError.AuthenticationError(throwable.message ?: "Authentication error")
//            else -> AppError.GenericError(throwable)
//        }
//    }
//
//    val result: Result<String> = Result.failure(TimeoutException())
////    val result: Result<String> = Result.success("Success!!")
//    val operationResult = OperationResultConfig.toOperationResult(result)
//    println(operationResult.getOrNull())
//
//}