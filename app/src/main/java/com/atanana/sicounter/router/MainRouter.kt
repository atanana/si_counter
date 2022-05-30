package com.atanana.sicounter.router

import android.content.Intent
import com.atanana.sicounter.screens.history.HistoryActivity
import com.atanana.sicounter.screens.main.MainActivity

class MainRouter(private val activity: MainActivity) {

    fun showSaveFileDialog(filename: String) {
        activity.createLogContract.launch(filename)
    }

    fun openHistory() {
        val intent = Intent(activity, HistoryActivity::class.java)
        activity.startActivity(intent)
    }

    fun close() {
        activity.finish()
    }
}
