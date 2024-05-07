package com.atanana.sicounter.screens.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.atanana.sicounter.R
import com.atanana.sicounter.databinding.ActivityMainBinding
import com.atanana.sicounter.databinding.DialogAddPlayerBinding
import com.atanana.sicounter.router.CreateLogFileContract
import com.atanana.sicounter.screens.history.HistoryActivity
import com.atanana.sicounter.utils.screenSize
import com.atanana.sicounter.view.player_control.PlayerControl
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), MainView, AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val scoresPresenter: ScoresPresenter by inject()
    private val presenter: MainUiPresenter by inject()

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var viewBinding: ActivityMainBinding

    val createLogContract = registerForActivityResult(CreateLogFileContract()) { uri ->
        lifecycleScope.launch {
            mainViewModel.saveLog(uri)
        }
    }

    override var selectedPrice: Int
        get() = viewBinding.content.priceSelector.price
        set(value) {
            viewBinding.content.priceSelector.price = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.content.scoresContainer.minimumWidth = screenSize(this).width

        mainViewModel.actions.flowWithLifecycle(lifecycle)
            .onEach(::handleAction)
            .launchIn(lifecycleScope)

        mainViewModel.history.flowWithLifecycle(lifecycle)
            .onEach(::updateHistory)
            .launchIn(lifecycleScope)

        viewBinding.addPlayer.setOnClickListener { showAddPlayerDialog() }
        viewBinding.content.addDivider.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.addDivider()
            }
        }
        viewBinding.content.noAnswer.setOnClickListener {
            lifecycleScope.launch {
                scoresPresenter.onNoAnswer()
            }
        }

        scoresPresenter.connect(lifecycleScope)

        onBackPressedDispatcher.addCallback {
            showQuitDialog()
        }
    }

    private fun handleAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.ShowSaveHistoryDialog -> createLogContract.launch(action.filename)
            MainScreenAction.Close -> finish()
        }
    }

    private fun updateHistory(history: List<String>) {
        val text = history.joinToString("\n")
        viewBinding.content.logView.updateText(text)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.mi_new -> {
                showResetDialog()
                true
            }

            R.id.mi_save -> {
                mainViewModel.saveHistory()
                true
            }

            R.id.mi_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveToBundle(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lifecycleScope.launch { presenter.restoreFromBundle(savedInstanceState) }
    }

    override fun showAddPlayerDialog() {
        val dialogBinding = DialogAddPlayerBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle(R.string.player_name_title)
            .setCancelable(true)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.ok) { _, _ ->
                val name = dialogBinding.name.text?.toString() ?: ""
                if (name.isNotEmpty()) {
                    lifecycleScope.launch { mainViewModel.addPlayer(name) }
                }
            }
            .show()
        dialogBinding.name.requestFocus()
    }

    override fun showResetDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reset_title)
            .setCancelable(true)
            .setMessage(R.string.reset_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                lifecycleScope.launch { mainViewModel.reset() }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun showQuitDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.close_title)
            .setCancelable(true)
            .setMessage(R.string.close_message)
            .setPositiveButton(R.string.yes) { _, _ -> mainViewModel.finish() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun appendLogs(line: String) {
        viewBinding.content.logView.append(line)
    }

    override fun addPlayerControl(playerControl: PlayerControl) {
        viewBinding.content.scoresContainer.addView(playerControl)
    }
}
