package com.atanana.sicounter.screens.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.atanana.sicounter.R
import com.atanana.sicounter.databinding.ActivityMainBinding
import com.atanana.sicounter.router.CreateLogFileContract
import com.atanana.sicounter.view.player_control.PlayerControl
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), MainView, AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val scoresPresenter: ScoresPresenter by inject()
    private val presenter: MainUiPresenter by inject()

    private lateinit var viewBinding: ActivityMainBinding

    val createLogContract = registerForActivityResult(CreateLogFileContract()) { uri ->
        lifecycleScope.launch {
            presenter.saveLog(uri)
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

        viewBinding.addPlayer.setOnClickListener { presenter.onFabClick() }
        viewBinding.content.addDivider.setOnClickListener {
            lifecycleScope.launch {
                presenter.addDivider()
            }
        }

        scoresPresenter.connect(lifecycleScope)
        lifecycleScope.launch { presenter.connect() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        presenter.toolbarItemSelected(item.itemId) || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        presenter.onBackPressed()
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
        AlertDialog.Builder(this)
            .setTitle(R.string.player_name_title)
            .setCancelable(true)
            .setView(R.layout.dialog_add_player)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                val playerName = (dialog as AlertDialog).findViewById<TextView>(R.id.name)
                val name = playerName!!.text.toString()
                lifecycleScope.launch { presenter.addPlayer(name) }
            }
            .show()
    }

    override fun showResetDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reset_title)
            .setCancelable(true)
            .setMessage(R.string.reset_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                lifecycleScope.launch { presenter.reset() }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun showQuitDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.close_title)
            .setCancelable(true)
            .setMessage(R.string.close_message)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.quit() }
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
