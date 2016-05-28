package com.atanana.sicounter.presenter

import com.atanana.sicounter.view.ScoresLog
import rx.Observable

class LogsPresenter(private val historyChanges: Observable<String>,
                    private val logsView: ScoresLog) {

    init {
        historyChanges.subscribe({ change -> logsView.append(change + "\n") })
    }
}