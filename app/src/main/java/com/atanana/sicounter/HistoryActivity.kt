package com.atanana.sicounter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.atanana.sicounter.di.HistoryUiModule
import com.atanana.sicounter.presenter.HistoryPresenter
import kotlinx.android.synthetic.main.activity_history.*
import javax.inject.Inject

class HistoryActivity : AppCompatActivity() {
    @Inject
    lateinit var historyPresenter: HistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val historyComponent = App.graph.historyComponent(HistoryUiModule(this))
        historyComponent.inject(this)
        historyPresenter.loadHistory()
    }
}
