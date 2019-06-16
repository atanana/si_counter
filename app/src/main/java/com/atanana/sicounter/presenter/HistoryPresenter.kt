package com.atanana.sicounter.presenter

import android.widget.TextView
import com.atanana.sicounter.fs.HistoryPersistence

class HistoryPresenter(
    private val historyPersistence: HistoryPersistence,
    private val historyView: TextView
) {
    fun loadHistory() {
        val history = historyPersistence.getAllHistory()
        historyView.text = history
    }

    fun clearHistory() {
        historyPersistence.clearHistory()
    }
}