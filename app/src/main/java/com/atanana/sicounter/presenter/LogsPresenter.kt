package com.atanana.sicounter.presenter

import com.atanana.sicounter.view.ScoresLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class LogsPresenter(
    private val historyChanges: Observable<String>
) {
    fun connect(logsView: ScoresLog): Disposable =
        historyChanges
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { change -> logsView.append(change + "\n") }
}