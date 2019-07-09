package com.atanana.sicounter.screens.main

import android.net.Uri
import android.os.Bundle
import com.atanana.sicounter.R
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.usecases.SaveLogUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

    private fun CoroutineScope.watchLogs() {
        launch {
            for (change in historyModel.historyChangesChannel) {
                view.appendLogs(change + "\n")
            }
        }
    }

    suspend fun connect() = coroutineScope {
        watchLogs()
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