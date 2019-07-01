package com.atanana.sicounter.presenter

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import io.reactivex.subjects.PublishSubject

class MainUiPresenter(
    private val newPlayers: PublishSubject<String>,
    private val saveLogPresenter: SaveFilePresenter,
    private val scoresModel: ScoresModel,
    private val historyModel: HistoryModel,
    private val activity: MainActivity
) {
    fun addDivider() {
        historyModel.addDivider()
    }

    fun showAddPlayerDialog() {
        val playerName = EditText(activity)
        playerName.setSingleLine()
        AlertDialog.Builder(activity)
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
                saveLogPresenter.showDialog()
                true
            }
            R.id.mi_history -> {
                val intent = Intent(activity, HistoryActivity::class.java)
                activity.startActivity(intent)
                true
            }
            else -> false
        }

    private fun showResetDialog() {
        AlertDialog.Builder(activity)
            .setTitle(R.string.reset_title)
            .setCancelable(true)
            .setMessage(R.string.reset_message)
            .setPositiveButton(R.string.yes) { _, _ -> scoresModel.reset() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    fun onBackPressed() {
        AlertDialog.Builder(activity)
            .setTitle(R.string.close_title)
            .setCancelable(true)
            .setMessage(R.string.close_message)
            .setPositiveButton(R.string.yes) { _, _ -> activity.finish() }
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