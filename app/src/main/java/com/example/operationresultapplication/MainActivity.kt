package com.example.operationresultapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.relatablecode.operationresult.toOperationResult
import java.lang.Exception
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Result.failure<Exception>(TimeoutException()).toOperationResult().onError {
            findViewById<TextView>(R.id.tv_hello_world).text = it.message
        }

    }

}
