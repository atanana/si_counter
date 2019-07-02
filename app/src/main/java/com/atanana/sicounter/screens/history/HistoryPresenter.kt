package com.atanana.sicounter.screens.history

import androidx.annotation.IdRes
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.HistoryPersistence

class HistoryPresenter(
    private val historyPersistence: HistoryPersistence,
    private val view: HistoryView,
    private val router: HistoryRouter
) {
    fun loadHistory() {
        val history = historyPersistence.getAllHistory()
        view.history.text = history.asReversed().joinToString("\n")
    }

    fun onOptionsItemSelected(@IdRes itemId: Int?): Boolean =
        when (itemId) {
            R.id.mi_clear_history -> {
                clearHistory()
                loadHistory()
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