package com.atanana.sicounter.presenter

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.widget.Button
import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.di.MainComponent
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import javax.inject.Inject
import javax.inject.Named

class MainUiPresenter(mainComponent: MainComponent) {
    @field:[Inject Named("exitDialog")]
    lateinit var exitDialog: AlertDialog.Builder

    @field:[Inject Named("resetDialog")]
    lateinit var resetDialog: AlertDialog.Builder

    @field:[Inject Named("addPlayerDialog")]
    lateinit var addPlayerDialog: AlertDialog.Builder

    @Inject
    lateinit var fabButton: FloatingActionButton

    @Inject
    lateinit var saveLogPresenter: SaveFilePresenter

    @Inject
    lateinit var scoresModel: ScoresModel

    @Inject
    lateinit var historyModel: HistoryModel

    @field:[Inject Named("addDivider")]
    lateinit var addDivider: Button

    @Inject
    lateinit var activity: MainActivity

    init {
        mainComponent.inject(this)

        fabButton.setOnClickListener {
            addPlayerDialog.show()
        }

        addDivider.setOnClickListener {
            historyModel.addDivider()
        }
    }

    fun toolbarItemSelected(itemId: Int): Boolean =
        when (itemId) {
            R.id.mi_new -> {
                resetDialog.show()
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

    fun onBackPressed() {
        exitDialog.show()
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