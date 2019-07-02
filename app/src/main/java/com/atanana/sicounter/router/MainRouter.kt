package com.atanana.sicounter.router

import android.app.Activity
import android.content.Intent
import com.atanana.sicounter.screens.history.HistoryActivity

class MainRouter(private val activity: Activity) {
    fun showSaveFileDialog(filename: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            .apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, filename)
                addCategory(Intent.CATEGORY_OPENABLE)
            }
        activity.startActivityForResult(intent, REQUEST_CODE_SAVE_FILE)
    }

    fun openHistory() {
        val intent = Intent(activity, HistoryActivity::class.java)
        activity.startActivity(intent)
    }

    fun close() {
        activity.finish()
    }

    companion object {
        const val REQUEST_CODE_SAVE_FILE = 1234
    }
}