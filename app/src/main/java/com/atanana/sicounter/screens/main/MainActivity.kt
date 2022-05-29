package com.atanana.sicounter.screens.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.atanana.sicounter.R
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.view.player_control.PlayerControl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity(), MainView, AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val scoresPresenter: ScoresPresenter by inject()
    private val presenter: MainUiPresenter by inject()

    override var selectedPrice: Int
        get() = price_selector.price
        set(value) {
            price_selector.price = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        add_player.setOnClickListener {
            presenter.onFabClick()
        }
        add_divider.setOnClickListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainRouter.REQUEST_CODE_SAVE_FILE && resultCode == Activity.RESULT_OK && data != null) {
            CoroutineScope(EmptyCoroutineContext).launch {
                presenter.saveLog(data.data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
        log_view.append(line)
    }

    override fun addPlayerControl(playerControl: PlayerControl) {
        scores_container.addView(playerControl)
    }
}
