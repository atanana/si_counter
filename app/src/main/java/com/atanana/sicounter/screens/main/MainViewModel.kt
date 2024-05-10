package com.atanana.sicounter.screens.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.sicounter.data.NoAnswer
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.usecases.SaveLogUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val scoresModel: ScoresModel,
    private val historyModel: HistoryModel,
    private val logNameModel: LogNameModel,
    private val saveLogUseCase: SaveLogUseCase
) : ViewModel() {

    private val actionsChannel = Channel<MainScreenAction>()
    val actions = actionsChannel.receiveAsFlow()

    val scoreActions = scoresModel.actionsChannel.receiveAsFlow()

    val history = historyModel.history

    suspend fun saveLog(uri: Uri?) {
        saveLogUseCase.saveReport(uri)
    }

    suspend fun addDivider() {
        historyModel.addDivider()
    }

    suspend fun addPlayer(name: String) {
        scoresModel.addPlayer(name)
        logNameModel.onPlayerAdded(name)
    }

    suspend fun reset() {
        scoresModel.reset()
    }

    fun saveHistory() {
        actionsChannel.trySend(MainScreenAction.ShowSaveHistoryDialog(logNameModel.fullFilename))
    }

    fun onNoAnswer(currentPrice: Int) {
        viewModelScope.launch {
            scoresModel.onScoreAction(NoAnswer(currentPrice))
        }
    }

    fun onScoreAction(action: ScoreAction) {
        viewModelScope.launch {
            scoresModel.onScoreAction(action)
        }
    }

    fun finish() {
        actionsChannel.trySend(MainScreenAction.Close)
    }
}

sealed interface MainScreenAction {

    data class ShowSaveHistoryDialog(val filename: String) : MainScreenAction

    data object Close : MainScreenAction
}
