package com.example.rateactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {
    private var frameLayout: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFields()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, RateFragment.newInstance())
            .commit()
    }

    private fun initFields() {
        frameLayout = findViewById(R.id.frameLayout)
    }
}