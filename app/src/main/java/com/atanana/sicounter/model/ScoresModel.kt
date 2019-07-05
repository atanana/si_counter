package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.*

const val KEY_SCORES: String = "scores_model_scores"

open class ScoresModel(
    private val historyModel: HistoryModel
) {
    private var playerScores: TreeMap<Int, Score> = TreeMap()
    private val updatedPlayers: Subject<Pair<Score, Int>> = PublishSubject.create()

    open val updatedPlayersObservable: Observable<Pair<Score, Int>> get() = updatedPlayers

    private val newPlayers: Channel<Pair<Score, Int>> = Channel()

    val newPlayersChannel: ReceiveChannel<Pair<Score, Int>> = newPlayers

    open val scores: List<Score>
        get() = playerScores.values.toList()

    open fun onScoreAction(action: ScoreAction) {
        val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
        val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
        playerScores[action.id] = newScore
        historyModel.onScoreAction(action, playerNameById(action.id))
        updatedPlayers.onNext(Pair(newScore, action.id))
    }

    suspend fun addPlayer(newPlayer: String) {
        val newScore = Score(newPlayer, 0)
        val newId = playerScores.size
        playerScores[newId] = newScore
        historyModel.onPlayerAdded(newPlayer)
        this.newPlayers.send(Pair(newScore, newId))
    }

    private fun playerNameById(id: Int): String {
        return playerScores[id]?.name ?: throw UnknownId(id)
    }

    open fun save(bundle: Bundle) {
        bundle.putSerializable(KEY_SCORES, playerScores)
    }

    open suspend fun restore(bundle: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        val newScores = bundle?.getSerializable(KEY_SCORES) as? TreeMap<Int, Score>
        if (newScores != null) {
            playerScores = newScores

            for ((id, score) in playerScores) {
                newPlayers.send(Pair(score, id))
            }
        }
    }

    fun reset() {
        for ((id, score) in playerScores) {
            val newScore = score.copy(score = 0)
            playerScores[id] = newScore
            updatedPlayers.onNext(Pair(newScore, id))
        }
        historyModel.reset()
    }
}