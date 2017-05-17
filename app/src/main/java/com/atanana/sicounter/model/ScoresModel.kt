package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject
import java.util.*

const val KEY_HISTORY: String = "scores_model_history"
const val KEY_SCORES: String = "scores_model_scores"

class ScoresModel(newPlayersNames: Observable<String>, private val scoreHistoryFormatter: ScoreHistoryFormatter) {
    private var playerScores: HashMap<Int, Score> = hashMapOf()
    private var _history: ArrayList<String> = arrayListOf()
    private val newPlayers: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    private val updatedPlayers: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    private val historyChanges: Subject<String, String> = PublishSubject()

    val newPlayersObservable get() = newPlayers

    val updatedPlayersObservable get() = updatedPlayers

    val historyChangesObservable get() = historyChanges

    init {
        newPlayersNames.subscribe({ newPlayer ->
            val newScore = Score(newPlayer, 0)
            val newId = playerScores.size
            playerScores.put(newId, newScore)
            addHistory(scoreHistoryFormatter.formatNewPlayer(newPlayer))
            newPlayers.onNext(Pair(newScore, newId))
        })
    }

    val history: List<String>
        get() {
            return Collections.unmodifiableList(_history)
        }

    fun subscribeToScoreActions(actions: Observable<ScoreAction>) {
        actions.subscribe({ action ->
            val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
            val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
            playerScores[action.id] = newScore
            addHistory(scoreHistoryFormatter.formatScoreAction(action, playerNameById(action.id)))
            updatedPlayers.onNext(Pair(newScore, action.id))
        })
    }

    private fun playerNameById(id: Int): String {
        return playerScores[id]?.name ?: throw UnknownId(id)
    }

    private fun addHistory(item: String) {
        _history.add(item)
        historyChanges.onNext(item)
    }

    fun save(bundle: Bundle) {
        bundle.putStringArrayList(KEY_HISTORY, _history)
        bundle.putSerializable(KEY_SCORES, playerScores)
    }

    fun restore(bundle: Bundle?) {
        val newHistory = bundle?.getStringArrayList(KEY_HISTORY)
        if (newHistory != null) {
            _history = newHistory

            for (item in _history) {
                historyChanges.onNext(item)
            }
        }

        @Suppress("UNCHECKED_CAST")
        val newScores = bundle?.getSerializable(KEY_SCORES) as? HashMap<Int, Score>
        if (newScores != null) {
            playerScores = newScores

            for ((id, score) in playerScores) {
                newPlayers.onNext(Pair(score, id))
            }
        }
    }

    fun reset() {
        for ((id, score) in playerScores) {
            val newScore = score.copy(score = 0)
            playerScores.put(id, newScore)
            updatedPlayers.onNext(Pair(newScore, id))
        }
        addHistory(scoreHistoryFormatter.resetMessage)
    }
}