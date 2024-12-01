package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.data.NoAnswer
import com.atanana.sicounter.data.ScoreChange
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.screens.main.ScoreHistoryFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

const val KEY_HISTORY: String = "scores_model_history"
const val HISTORY_SEPARATOR = "——————————"

class HistoryModel @Inject constructor(
    private val formatter: ScoreHistoryFormatter,
    private val historyPersistence: HistoryPersistence
) {
    private var _history = emptyList<String>()

    private val historyFlow = MutableStateFlow(_history)
    val history = historyFlow.asStateFlow()

    private suspend fun addHistory(item: String) {
        historyPersistence.addHistory(item)
        _history += item
        historyFlow.emit(_history)
    }

    suspend fun onPlayerAdded(player: String) {
        addHistory(formatter.formatNewPlayer(player))
    }

    suspend fun onScoreChange(action: ScoreChange, player: String) {
        addHistory(formatter.formatScoreChange(action, player))
    }

    suspend fun onNoAnswer(action: NoAnswer) {
        addHistory(formatter.formatNoAnswer(action))
    }

    suspend fun addDivider() {
        addHistory(HISTORY_SEPARATOR)
    }

    fun save(bundle: Bundle) {
        bundle.putStringArrayList(KEY_HISTORY, ArrayList(_history))
    }

    fun restore(bundle: Bundle?) {
        val newHistory = bundle?.getStringArrayList(KEY_HISTORY)
        if (newHistory != null) {
            _history = newHistory
            historyFlow.tryEmit(_history)
        }
    }

    suspend fun reset() {
        addHistory(formatter.resetMessage)
    }
}
