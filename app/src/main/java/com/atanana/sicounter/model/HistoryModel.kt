package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*

const val KEY_HISTORY: String = "scores_model_history"

open class HistoryModel(private val scoreHistoryFormatter: ScoreHistoryFormatter) {
    private var _history: ArrayList<String> = arrayListOf()

    private val historyChanges: Subject<String> = PublishSubject.create()

    val historyChangesObservable: Observable<String> get() = historyChanges

    val history: List<String>
        get() {
            return Collections.unmodifiableList(_history)
        }

    private fun addHistory(item: String) {
        _history.add(item)
        historyChanges.onNext(item)
    }

    open fun onPlayerAdded(player: String) {
        addHistory(scoreHistoryFormatter.formatNewPlayer(player))
    }

    open fun onScoreAction(action: ScoreAction, player: String) {
        addHistory(scoreHistoryFormatter.formatScoreAction(action, player))
    }

    fun save(bundle: Bundle) {
        bundle.putStringArrayList(KEY_HISTORY, _history)
    }

    open fun restore(bundle: Bundle?) {
        val newHistory = bundle?.getStringArrayList(KEY_HISTORY)
        if (newHistory != null) {
            _history = newHistory

            for (item in _history) {
                historyChanges.onNext(item)
            }
        }
    }

    open fun reset() {
        addHistory(scoreHistoryFormatter.resetMessage)
    }
}