package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.data.NoAnswer
import com.atanana.sicounter.data.ScoreChange
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.screens.main.ScoreHistoryFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.*

const val KEY_HISTORY: String = "scores_model_history"
const val HISTORY_SEPARATOR = "——————————"

class HistoryModel(
    private val formatter: ScoreHistoryFormatter,
    private val historyPersistence: HistoryPersistence
) {
    private var _history: ArrayList<String> = arrayListOf()

    private val historyChanges: Channel<String> = Channel()

    val historyChangesChannel: ReceiveChannel<String> = historyChanges

    val history: List<String>
        get() {
            return Collections.unmodifiableList(_history)
        }

    private suspend fun addHistory(item: String) {
        historyPersistence.addHistory(item)
        _history.add(item)
        historyChanges.send(item)
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
        bundle.putStringArrayList(KEY_HISTORY, _history)
    }

    suspend fun restore(bundle: Bundle?) {
        val newHistory = bundle?.getStringArrayList(KEY_HISTORY)
        if (newHistory != null) {
            _history = newHistory

            for (item in _history) {
                historyChanges.send(item)
            }
        }
    }

    suspend fun reset() {
        addHistory(formatter.resetMessage)
    }
}