package com.atanana.sicounter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.atanana.sicounter.presenter.HistoryPresenter
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.androidx.scope.currentScope

class HistoryActivity : AppCompatActivity() {
    private val historyPresenter: HistoryPresenter by currentScope.inject()

    lateinit var historyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        historyView = findViewById(R.id.history_content)
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
