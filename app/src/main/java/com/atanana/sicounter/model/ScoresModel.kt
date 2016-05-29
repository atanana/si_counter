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

const val KEY_HISTORY:String = "scores_model_history"
const val KEY_SCORES:String = "scores_model_scores"

class ScoresModel(private val newPlayersNames: Observable<String>, private val scoreHistoryFormatter: ScoreHistoryFormatter) {
    private var playerScores: HashMap<Int, Score> = hashMapOf()
    private var history: ArrayList<String> = arrayListOf()
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

    fun save(bundle: Bundle) {
        bundle.putStringArrayList(KEY_HISTORY, history)
        bundle.putSerializable(KEY_SCORES, playerScores)
    }

    fun restore(bundle: Bundle?) {
        val newHistory = bundle?.getStringArrayList(KEY_HISTORY)
        if (newHistory != null) {
            history = newHistory

            for (item in history) {
                historyChangesSubject.onNext(item)
            }
        }

        @Suppress("UNCHECKED_CAST")
        val newScores = bundle?.getSerializable(KEY_SCORES) as? HashMap<Int, Score>
        if (newScores != null) {
            playerScores = newScores

            for (playerScore in playerScores) {
                new.onNext(Pair(playerScore.value, playerScore.key))
            }
        }
    }
}