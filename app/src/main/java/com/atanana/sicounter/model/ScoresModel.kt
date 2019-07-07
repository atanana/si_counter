package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.UnknownId
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.*

const val KEY_SCORES: String = "scores_model_scores"

class ScoresModel(
    private val historyModel: HistoryModel
) {
    private var playerScores: TreeMap<Int, Score> = TreeMap()

    private val updatedPlayers: Channel<Pair<Score, Int>> = Channel()
    val updatedPlayersChannel: ReceiveChannel<Pair<Score, Int>> = updatedPlayers

    private val newPlayers: Channel<Pair<Score, Int>> = Channel()
    val newPlayersChannel: ReceiveChannel<Pair<Score, Int>> = newPlayers

    val scores: List<Score>
        get() = playerScores.values.toList()

    suspend fun onScoreAction(action: ScoreAction) {
        val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
        val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
        playerScores[action.id] = newScore
        historyModel.onScoreAction(action, playerNameById(action.id))
        updatedPlayers.send(Pair(newScore, action.id))
    }

    suspend fun addPlayer(newPlayer: String) {
        val newScore = Score(newPlayer, 0)
        val newId = playerScores.size
        playerScores[newId] = newScore
        historyModel.onPlayerAdded(newPlayer)
        newPlayers.send(Pair(newScore, newId))
    }

    private fun playerNameById(id: Int): String {
        return playerScores[id]?.name ?: throw UnknownId(id)
    }

    fun save(bundle: Bundle) {
        bundle.putSerializable(KEY_SCORES, playerScores)
    }

    suspend fun restore(bundle: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        val newScores = bundle?.getSerializable(KEY_SCORES) as? TreeMap<Int, Score>
        if (newScores != null) {
            playerScores = newScores

            for ((id, score) in playerScores) {
                newPlayers.send(Pair(score, id))
            }
        }
    }

    suspend fun reset() {
        for ((id, score) in playerScores) {
            val newScore = score.copy(score = 0)
            playerScores[id] = newScore
            updatedPlayers.send(Pair(newScore, id))
        }
        historyModel.reset()
    }
}