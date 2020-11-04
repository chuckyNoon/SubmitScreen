package com.example.rateactivity

import android.app.Application

class HeaderStateSource(private val app: Application) {
    fun getState(): HeaderState {
        val res = app.resources
        return HeaderState(
            res.getString(R.string.header_title),
            res.getString(R.string.header_second_line),
            res.getString(R.string.header_third_line),
            0
        )
    }
}

class HeaderState(
    var title: String,
    var secondLine: String,
    var thirdLine: String,
    var rating: Int
)
