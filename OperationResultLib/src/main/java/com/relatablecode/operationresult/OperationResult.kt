package com.relatablecode.operationresult

/**
 * A generic sealed class to encapsulate either a successful operation with its value (V)
 * or an error with its error type (E).
 */
sealed class OperationResult<out E: BaseError, out V> {

    /**
     * Represents a failed operation with its error type
     */
    data class Error<out E: BaseError>(val error: E) : OperationResult<E, Nothing>()

    /**
     * Represents a successful operation with its result value
     */
    data class Success<out V>(val value: V) : OperationResult<Nothing, V>()

    /**
     * Utility function to conditionally apply transformations based on whether it's an Error or a Success.
     * Useful for branching logic without having to use when{} every time.
     */
    fun <T> fold(ifError: (E) -> T, ifSuccess: (V) -> T): T {
        return when (this) {
            is Error -> ifError(error)
            is Success -> ifSuccess(value)
        }
    }

    /**
     * Returns the successful result or null if it was an error.
     */
    fun getOrNull(): V? {
        return when (this) {
            is Error -> null
            is Success -> value
        }
    }

    /**
     * Returns the error or null if it was a successful operation.
     */
    fun errorOrNull(): E? {
        return when (this) {
            is Error -> error
            else -> null
        }
    }

    /**
     * Combines multiple OperationResults into a single OperationResult containing a list of success values.
     * Returns the first encountered error if any of the results were errors.
     */
    fun <E: BaseError, V> combine(results: List<OperationResult<E, V>>): OperationResult<E, List<V>> {
        val values = mutableListOf<V>()
        for (result in results) {
            when (result) {
                is OperationResult.Error -> return result
                is OperationResult.Success -> values.add(result.value)
            }
        }
        return OperationResult.Success(values)
    }

    /**
     * Executes the provided action if the result was a success.
     */
    fun onValue(action: (V) -> Unit) {
        if (this is Success) action(value)
    }

    /**
     * Executes the provided action if the result was an error.
     */
    fun onError(action: (E) -> Unit) {
        if (this is Error) action(error)
    }

    /**
     * Returns true if the result represents an error.
     */
    fun isError() = this is OperationResult.Error

    /**
     * Returns true if the result represents a success.
     */
    fun isSuccess() = this is OperationResult.Success

    /**
     * Transforms the error of this result into another type, preserving the success type.
     */
    fun <F: BaseError> mapError(transform: (E) -> F): OperationResult<F, V> {
        return when (this) {
            is Error -> Error(transform(error))
            is Success -> Success(value)
        }
    }

    /**
     * Transforms the success value of this result into another type, preserving the error type.
     */
    fun <U> mapSuccess(transform: (V) -> U): OperationResult<E, U> {
        return when (this) {
            is Error -> Error(error)
            is Success -> Success(transform(value))
        }
    }

    /**
     * Transforms the result using the given transformation function.
     */
    inline fun <U> transform(transformer: (OperationResult<E, V>) -> U): U = transformer(this)

    /**
     * If this result represents a success, invokes the given action with the value.
     */
    fun ifSuccess(action: (V) -> Unit): OperationResult<E, V> {
        this.onValue(action)
        return this
    }

    /**
     * If this result represents an error, invokes the given action with the error.
     */
    fun ifError(action: (E) -> Unit): OperationResult<E, V> {
        this.onError(action)
        return this
    }

}
