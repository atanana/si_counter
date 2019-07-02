package com.atanana.sicounter.presenter

import android.widget.TextView
import androidx.annotation.IdRes
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.router.HistoryRouter

class HistoryPresenter(
    private val historyPersistence: HistoryPersistence,
    private val router: HistoryRouter
) {
    fun loadHistory(historyView: TextView) {
        val history = historyPersistence.getAllHistory()
        historyView.text = history.asReversed().joinToString("\n")
    }

    fun onOptionsItemSelected(@IdRes itemId: Int?, historyView: TextView): Boolean =
        when (itemId) {
            R.id.mi_clear_history -> {
                clearHistory()
                loadHistory(historyView)
                true
            }
            android.R.id.home -> {
                router.close()
                true
            }
            else -> false
        }

    private fun clearHistory() {
        historyPersistence.clearHistory()
    }
}