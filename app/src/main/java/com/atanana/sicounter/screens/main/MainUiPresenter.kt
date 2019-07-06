package com.atanana.sicounter.screens.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.atanana.sicounter.R
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.usecases.SaveLogUseCase
import com.atanana.sicounter.view.ScoresLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainUiPresenter(
    private val scoresModel: ScoresModel,
    private val historyModel: HistoryModel,
    private val router: MainRouter,
    private val logNameModel: LogNameModel,
    private val saveLogUseCase: SaveLogUseCase,
    private val uiScope: CoroutineScope
) {
    suspend fun saveLog(uri: Uri?) {
        saveLogUseCase.saveReport(uri)
    }

    suspend fun addDivider() {
        historyModel.addDivider()
    }

    fun showAddPlayerDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(R.string.player_name_title)
            .setCancelable(true)
            .setView(R.layout.dialog_add_player)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                val playerName = (dialog as AlertDialog).findViewById<TextView>(R.id.name)
                val name = playerName!!.text.toString()
                addPlayer(name)
            }
            .show()
    }

    private fun addPlayer(name: String) {
        uiScope.launch {
            scoresModel.addPlayer(name)
            logNameModel.onPlayerAdded(name)
        }
    }

    fun toolbarItemSelected(itemId: Int, context: Context): Boolean =
        when (itemId) {
            R.id.mi_new -> {
                showResetDialog(context)
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

    private fun showResetDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(R.string.reset_title)
            .setCancelable(true)
            .setMessage(R.string.reset_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                uiScope.launch { scoresModel.reset() }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    fun onBackPressed(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(R.string.close_title)
            .setCancelable(true)
            .setMessage(R.string.close_message)
            .setPositiveButton(R.string.yes) { _, _ -> router.close() }
            .setNegativeButton(R.string.no, null)
            .show()
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