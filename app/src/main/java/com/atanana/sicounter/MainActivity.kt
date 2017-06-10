package com.atanana.sicounter

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

    lateinit var mainUiPresenter: MainUiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        val mainComponent = App.graph.mainComponent(LogModule(), ScoresModule(), MainUiModule(this))
        mainComponent.inject(this)
        mainUiPresenter = MainUiPresenter(mainComponent)

        handleCrashes(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return mainUiPresenter.toolbarItemSelected(item.itemId) || return super.onOptionsItemSelected(item)
    }

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
}
