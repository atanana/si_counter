package com.atanana.sicounter

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.atanana.sicounter.di.LogModule
import com.atanana.sicounter.di.MainUiModule
import com.atanana.sicounter.di.ScoresModule
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.presenter.LogsPresenter
import com.atanana.sicounter.presenter.SaveFilePresenter
import com.atanana.sicounter.presenter.ScoresPresenter
import javax.inject.Inject
import javax.inject.Named

open class MainActivity : AppCompatActivity() {
    @field:[Inject Named("addPlayerDialog")]
    lateinit var addPlayerDialog: AlertDialog.Builder

    @field:[Inject Named("exitDialog")]
    lateinit var exitDialog: AlertDialog.Builder

    @field:[Inject Named("resetDialog")]
    lateinit var resetDialog: AlertDialog.Builder

    @Suppress("unused")
    @Inject
    lateinit var logsPresenter: LogsPresenter

    @Inject
    lateinit var scoresModel: ScoresModel

    @Inject
    lateinit var historyModel: HistoryModel

    @Inject
    lateinit var fabButton: FloatingActionButton

    @Suppress("unused")
    @Inject
    lateinit var scoresPresenter: ScoresPresenter

    @Inject
    lateinit var saveLogPresenter: SaveFilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        App.graph
                .mainComponent(LogModule(), ScoresModule(), MainUiModule(this))
                .inject(this)

        fabButton.setOnClickListener {
            addPlayerDialog.show()
        }

        handleCrashes(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_new -> {
                resetDialog.show()
                return true
            }
            R.id.mi_save -> {
                saveLogPresenter.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        exitDialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scoresModel.save(outState)
        historyModel.save(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        scoresModel.restore(savedInstanceState)
        historyModel.restore(savedInstanceState)
    }
}
