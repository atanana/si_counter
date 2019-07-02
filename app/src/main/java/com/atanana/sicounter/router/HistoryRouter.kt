package com.atanana.sicounter.router

import android.app.Activity

class HistoryRouter(private val activity: Activity) {
    fun close() {
        activity.onBackPressed()
    }
}