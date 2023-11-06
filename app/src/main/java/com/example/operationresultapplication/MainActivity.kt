package com.example.operationresultapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.relatablecode.operationresult.AppError
import com.relatablecode.operationresult.OperationResult
import com.relatablecode.operationresult.OperationResultConfig
import com.relatablecode.operationresult.toOperationResult
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        OperationResultConfig.initialize().withBaseError { throwable: Throwable ->
            when (throwable) {
                is TimeoutException -> AppError.TimeoutError(throwable.message ?: "Request timed out from Main Activity")
                else -> AppError.GenericError(throwable)
            }
        }

        val result: Result<String> = someFunctionThatCanFail()
        val operationResult = result.toOperationResult()

        when (operationResult) {
            is OperationResult.Success -> {
                println("Success with value: ${operationResult.value}")
            }
            is OperationResult.Error -> {
                println("Error occurred: ${operationResult.error}")
            }
        }

    }

    fun someFunctionThatCanFail(): Result<String> {
        // Logic that can throw exceptions
        return Result.failure(TimeoutException())
    }

}
