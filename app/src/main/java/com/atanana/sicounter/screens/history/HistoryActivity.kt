package com.atanana.sicounter.screens.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.databinding.ActivityHistoryBinding
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

class HistoryActivity : AppCompatActivity(), HistoryView, AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val historyPresenter: HistoryPresenter by inject()

    private lateinit var viewBinding: ActivityHistoryBinding

    override val history: TextView
        get() = viewBinding.content.historyContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
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
