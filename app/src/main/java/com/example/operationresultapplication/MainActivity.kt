package com.example.operationresultapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.relatablecode.operationresult.OperationResult
import com.relatablecode.operationresult.toOperationResult
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    }

    fun someFunctionThatCanFail(): Result<String> {
        // Logic that can throw exceptions
        return Result.failure(Exception("Some error happened"))
    }

}
