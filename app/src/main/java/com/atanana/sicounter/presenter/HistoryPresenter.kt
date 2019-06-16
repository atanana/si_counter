package com.atanana.sicounter.presenter

import androidx.annotation.IdRes
import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.HistoryPersistence

class HistoryPresenter(
    private val historyPersistence: HistoryPersistence,
    private val activity: HistoryActivity
) {
    fun loadHistory() {
        val history = historyPersistence.getAllHistory()
        activity.historyView.text = history.asReversed().joinToString("\n")
    }

    fun onOptionsItemSelected(@IdRes itemId: Int?): Boolean =
        when (itemId) {
            R.id.mi_clear_history -> {
                clearHistory()
                loadHistory()
                true
            }
            android.R.id.home -> {
                activity.onBackPressed()
                true
            }
            else -> false
        }

    private fun clearHistory() {
        historyPersistence.clearHistory()
    }
}