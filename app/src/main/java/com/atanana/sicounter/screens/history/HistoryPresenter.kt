package com.atanana.sicounter.screens.history

import androidx.annotation.IdRes
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.HistoryPersistence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HistoryPresenter(
    private val historyPersistence: HistoryPersistence,
    private val view: HistoryView,
    private val router: HistoryRouter
) {
    suspend fun loadHistory() {
        val history = historyPersistence.getAllHistory()
        view.history.text = history.asReversed().joinToString("\n")
    }

    fun onOptionsItemSelected(@IdRes itemId: Int?, scope: CoroutineScope): Boolean =
        when (itemId) {
            R.id.mi_clear_history -> {
                scope.launch {
                    clearHistory()
                    loadHistory()
                }
                true
            }

            android.R.id.home -> {
                router.close()
                true
            }

            else -> false
        }

    private suspend fun clearHistory() {
        historyPersistence.clearHistory()
    }
}
