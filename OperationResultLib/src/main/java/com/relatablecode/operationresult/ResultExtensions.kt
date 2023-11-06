package com.relatablecode.operationresult


/**
 * Transforms a Kotlin standard Result into an OperationResult with appropriate error types.
 */
inline fun <reified E : BaseError, T> Result<T>.toCustomOperationResult(noinline customHandler: ((Throwable) -> E)? = null): OperationResult<E, T> {
    return this.fold(
        onSuccess = {
            OperationResult.Success(it)
        },
        onFailure = { throwable ->
            val error = handleThrowable(throwable, customHandler)
            OperationResult.Error(error)
        }
    )
}

/**
 * Transforms a Kotlin standard Result into an OperationResult with AppError.
 */
fun <T> Result<T>.toOperationResult(): OperationResult<BaseError, T> {
    return if (OperationResultConfig.isInitialized) {
        OperationResultConfig.toOperationResult(this)
    } else {
        this.fold(
            onSuccess = {
                OperationResult.Success(it)
            },
            onFailure = { throwable ->
                val error = handleThrowable<AppError>(throwable)
                OperationResult.Error(error)
            }
        )
    }
}
