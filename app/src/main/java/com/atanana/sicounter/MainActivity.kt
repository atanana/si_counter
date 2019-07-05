package com.atanana.sicounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.atanana.sicounter.presenter.LogsPresenter
import com.atanana.sicounter.presenter.MainUiPresenter
import com.atanana.sicounter.presenter.ScoresPresenter
import com.atanana.sicounter.router.MainRouter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.EmptyCoroutineContext

open class MainActivity : AppCompatActivity() {
    private val uiScope = MainScope()

    private val logsPresenter: LogsPresenter by currentScope.inject()
    private val scoresPresenter: ScoresPresenter by currentScope.inject()

    private val mainRouter: MainRouter by currentScope.inject { parametersOf(this) }
    private val mainUiPresenter: MainUiPresenter by currentScope.inject { parametersOf(mainRouter, uiScope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        add_player.setOnClickListener {
            mainUiPresenter.showAddPlayerDialog(this)
        }
        add_divider.setOnClickListener {
            uiScope.launch {
                mainUiPresenter.addDivider()
            }
        }

        uiScope.launch {
            scoresPresenter.connect(price_selector, scores_container)
            logsPresenter.connect(log_view)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        mainUiPresenter.toolbarItemSelected(item.itemId, this) || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        mainUiPresenter.onBackPressed(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainUiPresenter.saveToBundle(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        uiScope.launch { mainUiPresenter.restoreFromBundle(savedInstanceState) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainRouter.REQUEST_CODE_SAVE_FILE && resultCode == Activity.RESULT_OK && data != null) {
            CoroutineScope(EmptyCoroutineContext).launch {
                mainUiPresenter.saveLog(data.data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
    }
}
