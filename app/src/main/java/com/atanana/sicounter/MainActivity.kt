package com.atanana.sicounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
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
import com.atanana.sicounter.presenter.MainUiPresenter
import com.atanana.sicounter.presenter.SaveFilePresenter
import com.atanana.sicounter.presenter.ScoresPresenter
import javax.inject.Inject

open class MainActivity : AppCompatActivity() {
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
    lateinit var saveFilePresenter: SaveFilePresenter

    lateinit var mainUiPresenter: MainUiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val mainComponent = App.graph.mainComponent(LogModule(), ScoresModule(), MainUiModule(this))
        mainComponent.inject(this)
        mainUiPresenter = MainUiPresenter(mainComponent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        mainUiPresenter.toolbarItemSelected(item.itemId) || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        mainUiPresenter.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainUiPresenter.saveToBundle(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        mainUiPresenter.restoreFromBundle(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SaveFilePresenter.REQUEST_CODE_SAVE_FILE && resultCode == Activity.RESULT_OK && data != null) {
            saveFilePresenter.saveReport(data.data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
