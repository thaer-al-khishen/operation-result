# OperationResult Library

A flexible and comprehensive Kotlin library for encapsulating the results of operations, making error handling more expressive and efficient.

## Features
- Sealed class-based result encapsulation.
- Extendable error handling mechanism.
- Kotlin idiomatic approach.
- Integrated with Kotlinx serialization for JSON error handling.

## Installation

```groovy
implementation 'com.github.thaer-al-khishen:operation-result:1.0.0-beta02'
```

## Usage

### Basic Usage

```kotlin
fun someFunctionThatCanFail(): Result<String> {
    // Logic that can throw exceptions
    return Result.failure(Exception("Some error happened"))
}
```

You can convert the result to an OperationResult:

```kotlin
val result: Result<String> = someFunctionThatCanFail()
val operationResult = result.toOperationResult()

when (operationResult) {
    is OperationResult.Success -> {
        println("Success with value: ${operationResult.value}")
    }
    is OperationResult.Error -> {
        println("Error occurred: ${operationResult.error.message}")
    }
}
```

## Folding Results
The fold function allows you to elegantly handle both success and error cases in one step:
```kotlin
operationResult.fold(
    ifError = { error -> 
        println("Error occurred: ${error.message}")
    },
    ifSuccess = { value -> 
        println("Success with value: $value")
    }
)
```

## Custom Error Handling
Define your own error types and handle them with the library:
```kotlin
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

val result: Result<String> = Result.failure(TimeoutException())
val operationResult = result.toCustomOperationResult(::customErrorHandler)
println(operationResult.errorOrNull()?.message)
```

## License:
This project is licensed under the Apache 2.0 License. Check the LICENSE file for details.
