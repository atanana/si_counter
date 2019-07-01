package com.atanana.sicounter.presenter

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.atanana.sicounter.R
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.router.MainRouter
import io.reactivex.subjects.PublishSubject

class MainUiPresenter(
    private val newPlayers: PublishSubject<String>,
    private val saveLogPresenter: SaveFilePresenter,
    private val scoresModel: ScoresModel,
    private val historyModel: HistoryModel,
    private val router: MainRouter,
    private val context: Context
) {
    fun addDivider() {
        historyModel.addDivider()
    }

    fun showAddPlayerDialog() {
        val playerName = EditText(context)
        playerName.setSingleLine()
        AlertDialog.Builder(context)
            .setTitle(R.string.player_name_title)
            .setCancelable(true)
            .setView(playerName)
            .setPositiveButton(R.string.ok) { _, _ ->
                newPlayers.onNext(playerName.text.toString())
            }
    }

    fun toolbarItemSelected(itemId: Int): Boolean =
        when (itemId) {
            R.id.mi_new -> {
                showResetDialog()
                true
            }
            R.id.mi_save -> {
                router.showSaveFileDialog(saveLogPresenter.filename)
                true
            }
            R.id.mi_history -> {
                router.openHistory()
                true
            }
            else -> false
        }

    private fun showResetDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.reset_title)
            .setCancelable(true)
            .setMessage(R.string.reset_message)
            .setPositiveButton(R.string.yes) { _, _ -> scoresModel.reset() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    fun onBackPressed() {
        AlertDialog.Builder(context)
            .setTitle(R.string.close_title)
            .setCancelable(true)
            .setMessage(R.string.close_message)
            .setPositiveButton(R.string.yes) { _, _ -> router.close() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    fun saveToBundle(outState: Bundle) {
        scoresModel.save(outState)
        historyModel.save(outState)
    }

    fun restoreFromBundle(savedInstanceState: Bundle?) {
        scoresModel.restore(savedInstanceState)
        historyModel.restore(savedInstanceState)
    }
}