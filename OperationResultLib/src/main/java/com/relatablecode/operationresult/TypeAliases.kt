package com.relatablecode.operationresult

/**
 * Alias for OperationResult where the error type is specifically an AppError.
 */
typealias GenericResponse<T> = OperationResult<AppError, T>
