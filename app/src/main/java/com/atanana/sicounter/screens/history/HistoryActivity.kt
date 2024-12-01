package com.atanana.sicounter.screens.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.atanana.sicounter.R
import com.atanana.sicounter.databinding.ActivityHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var viewBinding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        historyViewModel.actions
            .onEach(::handleAction)
            .launchIn(lifecycleScope)

        historyViewModel.historyState
            .onEach(::handleState)
            .launchIn(lifecycleScope)
    }

    private fun handleAction(action: HistoryAction) {
        when (action) {
            HistoryAction.Close -> finish()
        }
    }

    private fun handleState(state: List<String>) {
        viewBinding.content.historyContent.text = state.joinToString("\n")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.mi_clear_history -> {
                lifecycleScope.launch {
                    historyViewModel.clear()
                }
                true
            }

            android.R.id.home -> {
                historyViewModel.close()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
}
