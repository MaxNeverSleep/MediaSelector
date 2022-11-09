package com.max.mediaselector_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.max.mediaselector.TestUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TestUtils.testFunction()

    }
}