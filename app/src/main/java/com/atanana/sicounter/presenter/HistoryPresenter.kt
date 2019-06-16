package com.atanana.sicounter.presenter

import android.support.annotation.IdRes
import android.widget.TextView
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.HistoryPersistence

class HistoryPresenter(
    private val historyPersistence: HistoryPersistence,
    private val historyView: TextView
) {
    fun loadHistory() {
        val history = historyPersistence.getAllHistory()
        historyView.text = history
    }

    fun onOptionsItemSelected(@IdRes itemId: Int?): Boolean =
        when (itemId) {
            R.id.mi_clear_history -> {
                clearHistory()
                loadHistory()
                true
            }
            else -> false
        }

    private fun clearHistory() {
        historyPersistence.clearHistory()
    }
}