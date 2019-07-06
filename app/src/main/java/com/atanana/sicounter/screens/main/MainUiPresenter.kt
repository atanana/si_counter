package com.atanana.sicounter.screens.main

import android.net.Uri
import android.os.Bundle
import com.atanana.sicounter.R
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.usecases.SaveLogUseCase
import com.atanana.sicounter.view.ScoresLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainUiPresenter(
    private val view: MainView,
    private val router: MainRouter,
    private val scoresModel: ScoresModel,
    private val historyModel: HistoryModel,
    private val logNameModel: LogNameModel,
    private val saveLogUseCase: SaveLogUseCase
) {
    suspend fun saveLog(uri: Uri?) {
        saveLogUseCase.saveReport(uri)
    }

    suspend fun addDivider() {
        historyModel.addDivider()
    }

    fun onFabClick() {
        view.showAddPlayerDialog()
    }

    suspend fun addPlayer(name: String) {
        scoresModel.addPlayer(name)
        logNameModel.onPlayerAdded(name)
    }

    fun toolbarItemSelected(itemId: Int): Boolean =
        when (itemId) {
            R.id.mi_new -> {
                view.showResetDialog()
                true
            }
            R.id.mi_save -> {
                router.showSaveFileDialog(logNameModel.fullFilename)
                true
            }
            R.id.mi_history -> {
                router.openHistory()
                true
            }
            else -> false
        }

    suspend fun reset() {
        scoresModel.reset()
    }

    fun onBackPressed() {
        view.showQuitDialog()
    }

    fun quit() {
        router.close()
    }

    suspend fun watchLogs(logsView: ScoresLog) = withContext(Dispatchers.Default) {
        launch {
            for (change in historyModel.historyChangesChannel) {
                withContext(Dispatchers.Main) {
                    logsView.append(change + "\n")
                }
            }
        }
    }

    fun saveToBundle(outState: Bundle) {
        scoresModel.save(outState)
        historyModel.save(outState)
    }

    suspend fun restoreFromBundle(savedInstanceState: Bundle?) {
        scoresModel.restore(savedInstanceState)
        historyModel.restore(savedInstanceState)
    }
}