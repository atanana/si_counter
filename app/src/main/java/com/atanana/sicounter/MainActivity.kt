package com.atanana.sicounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.presenter.LogsPresenter
import com.atanana.sicounter.presenter.MainUiPresenter
import com.atanana.sicounter.presenter.SaveFilePresenter
import com.atanana.sicounter.presenter.ScoresPresenter
import com.atanana.sicounter.router.MainRouter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

open class MainActivity : AppCompatActivity() {
    private lateinit var disposable: CompositeDisposable

    private val logsPresenter: LogsPresenter by currentScope.inject()
    private val scoresModel: ScoresModel by currentScope.inject()
    private val scoresPresenter: ScoresPresenter by currentScope.inject()
    private val saveFilePresenter: SaveFilePresenter by currentScope.inject()

    private val mainRouter: MainRouter by currentScope.inject { parametersOf(this) }
    private val mainUiPresenter: MainUiPresenter by currentScope.inject { parametersOf(mainRouter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        add_player.setOnClickListener {
            mainUiPresenter.showAddPlayerDialog(this)
        }
        add_divider.setOnClickListener {
            mainUiPresenter.addDivider()
        }

        disposable = CompositeDisposable().apply {
            addAll(
                logsPresenter.connect(log_view),
                scoresModel.connect(),
                scoresPresenter.connect(price_selector, scores_container)
            )
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
        mainUiPresenter.restoreFromBundle(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainRouter.REQUEST_CODE_SAVE_FILE && resultCode == Activity.RESULT_OK && data != null) {
            saveFilePresenter.saveReport(data.data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
