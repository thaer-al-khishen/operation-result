package com.relatablecode.operationresult

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Wraps the result of an asynchronous operation into an OperationResult.
 * Logs success and error results.
 */
suspend inline fun <T> wrapWithOperationResult(
    crossinline action: suspend () -> T
): OperationResult<AppError, T> {

    return withContext(Dispatchers.IO) {
        val result = runCatching { action() }
        result.fold(
            onSuccess = {
                Log.d("Network Call wrap", "Success: ${it.toString()}")
                OperationResult.Success(it)
            },
            onFailure = { throwable ->
                Log.d("Network Call wrap", "Error: ${throwable.message.toString()}")
                val error = handleThrowable<AppError>(throwable)
                OperationResult.Error(error)
            }
        )
    }

}

/**
 * Wraps the result of an asynchronous operation into an OperationResult.
 * Logs success and error results.
 */
suspend inline fun <reified E: BaseError, T> wrapWithCustomOperationResult(
    noinline customHandler: ((Throwable) -> E)? = null,
    crossinline action: suspend () -> T
): OperationResult<E, T> {

    return withContext(Dispatchers.IO) {
        val result = runCatching { action() }
        result.fold(
            onSuccess = {
                Log.d("Network Call wrap", "Success: ${it.toString()}")
                OperationResult.Success(it)
            },
            onFailure = { throwable ->
                Log.d("Network Call wrap", "Error: ${throwable.message.toString()}")
                val error = handleThrowable(throwable, customHandler)
                OperationResult.Error(error)
            }
        )
    }

}
