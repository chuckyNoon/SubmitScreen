package com.example.rateactivity

import android.util.Log

class MainHelper() {
    companion object {
        fun log(tag: String, text: String) {
            Log.d(tag, text)
        }
    }
}


class RateLogger {
    companion object{
        private val LOG_ON = true
        fun log(msg: String) {
            if (LOG_ON) {
                MainHelper.log("RateLogger", msg)
            }
        }
    }
}