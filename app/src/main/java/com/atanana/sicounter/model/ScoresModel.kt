package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.reactivex.Observable
import java.util.*

const val KEY_SCORES: String = "scores_model_scores"

open class ScoresModel(newPlayersNames: Observable<String>, private val historyModel: HistoryModel) {
    private var playerScores: TreeMap<Int, Score> = TreeMap()
    private val newPlayers: Subject<Pair<Score, Int>> = PublishSubject.create()
    private val updatedPlayers: Subject<Pair<Score, Int>> = PublishSubject.create()

    open val newPlayersObservable: Observable<Pair<Score, Int>> get() = newPlayers

    open val updatedPlayersObservable: Observable<Pair<Score, Int>> get() = updatedPlayers

    init {
        newPlayersNames.subscribe({ newPlayer ->
            val newScore = Score(newPlayer, 0)
            val newId = playerScores.size
            playerScores.put(newId, newScore)
            historyModel.onPlayerAdded(newPlayer)
            newPlayers.onNext(Pair(newScore, newId))
        })
    }

    open fun subscribeToScoreActions(actions: Observable<ScoreAction>) {
        actions.subscribe({ action ->
            val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
            val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
            playerScores[action.id] = newScore
            historyModel.onScoreAction(action, playerNameById(action.id))
            updatedPlayers.onNext(Pair(newScore, action.id))
        })
    }

    private fun playerNameById(id: Int): String {
        return playerScores[id]?.name ?: throw UnknownId(id)
    }

    open fun save(bundle: Bundle) {
        bundle.putSerializable(KEY_SCORES, playerScores)
    }

    open fun restore(bundle: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        val newScores = bundle?.getSerializable(KEY_SCORES) as? TreeMap<Int, Score>
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
        historyModel.reset()
    }
}