package com.relatablecode.operationresult


/**
 * If the result represents a success, it returns the value, otherwise returns the provided default value.
 */
fun <V> OperationResult<*, V>.getOrDefault(defaultValue: V): V {
    return when (this) {
        is OperationResult.Success -> this.value
        is OperationResult.Error -> defaultValue
    }
}

/**
 * If the result represents a success, it returns the value, otherwise invokes the provided function with the error.
 */
fun <E: BaseError, V> OperationResult<E, V>.getOrElse(default: (E) -> V): V {
    return when (this) {
        is OperationResult.Success -> value
        is OperationResult.Error -> default(error)
    }
}

/**
 * Transforms the success value of this result into another OperationResult.
 * If the result is an error, it's returned unchanged.
 */
inline fun <A: BaseError, B, C> OperationResult<A, B>.flatMap(f: (right: B) -> OperationResult<A, C>): OperationResult<A, C> {
    return when (this) {
        is OperationResult.Success -> f(this.value)
        is OperationResult.Error -> this
    }
}

/**
 * Filters the success value of this result based on a predicate.
 * If the predicate is satisfied, returns the result unchanged, otherwise returns an error with the provided default error.
 */
fun <E: BaseError, V> OperationResult<E, V>.filterOrElse(
    predicate: (V) -> Boolean,
    default: () -> E
): OperationResult<E, V> =
    flatMap { if (predicate(it)) OperationResult.Success(it) else OperationResult.Error(default()) }

/**
 * Transforms an error into a success using the provided recovery function.
 */
fun <E: BaseError, V> OperationResult<E, V>.recover(recoverFn: (E) -> V): OperationResult<E, V> {
    return when (this) {
        is OperationResult.Success -> this
        is OperationResult.Error -> OperationResult.Success(recoverFn(error))
    }
}

/**
 * Transforms an error into another OperationResult using the provided recovery function.
 */
fun <E: BaseError, V> OperationResult<E, V>.recoverWith(recoverFn: (E) -> OperationResult<E, V>): OperationResult<E, V> {
    return when (this) {
        is OperationResult.Success -> this
        is OperationResult.Error -> recoverFn(error)
    }
}

/**
 * Transforms the success value of this result asynchronously into another OperationResult.
 */
suspend fun <E: BaseError, V, U> OperationResult<E, V>.thenAsync(
    transform: suspend (V) -> OperationResult<E, U>
): OperationResult<E, U> {
    return when (this) {
        is OperationResult.Success -> transform(this.value)
        is OperationResult.Error -> this
    }
}
