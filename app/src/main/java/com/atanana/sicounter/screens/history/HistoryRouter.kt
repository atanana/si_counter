package com.atanana.sicounter.screens.history

import android.app.Activity

class HistoryRouter(private val activity: Activity) {
    fun close() {
        activity.onBackPressed()
    }
}