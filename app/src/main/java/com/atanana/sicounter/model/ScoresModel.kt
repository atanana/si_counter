package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

class ScoresModel(private val newPlayersNames: Observable<String>, private val scoreHistoryFormatter: ScoreHistoryFormatter) {
    private val playerScores: MutableMap<Int, Score> = hashMapOf()
    private val history: MutableList<String> = arrayListOf()
    private val new: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    private val updated: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    private val historyChangesSubject: Subject<String, String> = PublishSubject()
    val newPlayers: Observable<Pair<Score, Int>>
        get() = new

    val updatedPlayers: Observable<Pair<Score, Int>>
        get() = updated

    val historyChanges: Observable<String>
        get() = historyChangesSubject

    init {
        newPlayersNames.subscribe({ newPlayer ->
            val newScore = Score(newPlayer, 0)
            val newId = playerScores.size
            playerScores.put(newId, newScore)
            addHistory(scoreHistoryFormatter.formatNewPlayer(newPlayer))
            new.onNext(Pair(newScore, newId))
        })
    }

    fun subscribeToScoreActions(actions: Observable<ScoreAction>) {
        actions.subscribe({ action ->
            val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
            val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
            playerScores[action.id] = newScore
            addHistory(scoreHistoryFormatter.formatScoreAction(action, playerNameById(action.id)))
            updated.onNext(Pair(newScore, action.id))
        })
    }

    private fun playerNameById(id: Int): String {
        return playerScores[id]?.name ?: throw UnknownId(id)
    }

    private fun addHistory(item: String) {
        history.add(item)
        historyChangesSubject.onNext(item)
    }
}