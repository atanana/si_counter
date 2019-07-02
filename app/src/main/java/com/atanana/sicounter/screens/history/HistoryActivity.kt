package com.atanana.sicounter.screens.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.atanana.sicounter.R
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

class HistoryActivity : AppCompatActivity(), HistoryView {
    private val router: HistoryRouter by currentScope.inject { parametersOf(this) }
    private val historyPresenter: HistoryPresenter by currentScope.inject { parametersOf(this, router) }

    override val history: TextView
        get() = history_content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        historyPresenter.loadHistory()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        historyPresenter.onOptionsItemSelected(item.itemId) || super.onOptionsItemSelected(item)
}
