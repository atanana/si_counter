package com.atanana.sicounter.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.sicounter.fs.HistoryPersistence
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val persistence: HistoryPersistence) : ViewModel() {

    private val historyStateFlow = MutableStateFlow(emptyList<String>())
    val historyState = historyStateFlow.asStateFlow()

    private val actionsChannel = Channel<HistoryAction>()
    val actions = actionsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val history = persistence.getAllHistory()
            historyStateFlow.emit(history.asReversed())
        }
    }

    suspend fun clear() {
        persistence.clearHistory()
        historyStateFlow.emit(emptyList())
    }

    fun close() {
        actionsChannel.trySend(HistoryAction.Close)
    }
}

sealed interface HistoryAction {

    data object Close : HistoryAction
}
