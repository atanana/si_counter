package com.atanana.sicounter

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import com.atanana.sicounter.logging.LoggerConfiguration
import com.atanana.sicounter.logging.LogsWriter
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.presenter.LogsPresenter
import com.atanana.sicounter.presenter.ScoreActionPriceTransformer.transform
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import com.atanana.sicounter.presenter.ScoresPresenter
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.ScoresLog
import com.atanana.sicounter.view.player_control.DefaultPlayerControlFabric
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

class MainActivity : AppCompatActivity() {
    private val priceSelector: PriceSelector by lazy { findViewById(R.id.price_selector) as PriceSelector }
    private val scoresContainer: ViewGroup by lazy { findViewById(R.id.scores_container) as ViewGroup }
    private val addPlayer: Subject<String, String> = PublishSubject()
    lateinit private var scoresModel: ScoresModel
    private val scoresPresenter: ScoresPresenter by lazy {
        ScoresPresenter(scoresModel, scoresContainer, DefaultPlayerControlFabric(this))
    }

    private val logsView: ScoresLog by lazy { findViewById(R.id.log_view) as ScoresLog }
    private val logsPresenter: LogsPresenter by lazy {
        LogsPresenter(scoresModel.historyChanges, logsView)
    }
    private val logsWriter: LogsWriter by lazy { LogsWriter(scoresModel.historyChanges) }

    private val addPlayerDialog: AlertDialog.Builder by lazy {
        val playerName = EditText(this)
        playerName.setSingleLine(true)
        AlertDialog.Builder(this)
                .setTitle(R.string.player_name_title)
                .setCancelable(true)
                .setView(playerName)
                .setPositiveButton(R.string.ok, { _, _ ->
                    addPlayer.onNext(playerName.text.toString())
                    playerName.text.clear()
                    (playerName.parent as? ViewGroup)?.removeView(playerName)
                })
    }

    private val exitDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle(R.string.close_title)
                .setCancelable(true)
                .setMessage(R.string.close_message)
                .setPositiveButton(R.string.yes, { _, _ -> finish() })
                .setNegativeButton(R.string.no, null)
    }

    private val resetDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle(R.string.reset_title)
                .setCancelable(true)
                .setMessage(R.string.reset_message)
                .setPositiveButton(R.string.yes, { _, _ -> scoresModel.reset() })
                .setNegativeButton(R.string.no, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoggerConfiguration.configureLogbackDirectly(applicationContext)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.add_player) as FloatingActionButton?
        fab!!.setOnClickListener {
            addPlayerDialog.show()
        }

        scoresModel = ScoresModel(addPlayer, ScoreHistoryFormatter(this))
        scoresModel.subscribeToScoreActions(transform(scoresPresenter.scoreActions, priceSelector))
        logsPresenter
        logsWriter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_new -> {
                resetDialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        exitDialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scoresModel.save(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        scoresModel.restore(savedInstanceState)
    }
}
