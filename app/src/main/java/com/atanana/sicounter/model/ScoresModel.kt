package com.atanana.sicounter.model

import android.os.Bundle
import com.atanana.sicounter.UnknownId
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.model.ScoreModelAction.NewPlayer
import com.atanana.sicounter.model.ScoreModelAction.UpdateScore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.util.*

const val KEY_SCORES: String = "scores_model_scores"

class ScoresModel(private val historyModel: HistoryModel) {

    private var playerScores: TreeMap<Int, Score> = TreeMap()

    private val actions = Channel<ScoreModelAction>()
    val actionsChannel: ReceiveChannel<ScoreModelAction> = actions

    val scores: List<Score>
        get() = playerScores.values.toList()

    suspend fun onScoreAction(action: ScoreAction) {
        val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
        val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
        playerScores[action.id] = newScore
        historyModel.onScoreAction(action, playerNameById(action.id))
        actions.send(UpdateScore(action.id, newScore))
    }

    suspend fun addPlayer(newPlayer: String) {
        val newScore = Score(newPlayer, 0)
        val newId = playerScores.size
        playerScores[newId] = newScore
        historyModel.onPlayerAdded(newPlayer)
        actions.send(NewPlayer(newId, newScore))
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
                actions.send(NewPlayer(id, score))
            }
        }
    }

    suspend fun reset() {
        for ((id, score) in playerScores) {
            val newScore = score.copy(score = 0)
            playerScores[id] = newScore
            actions.send(UpdateScore(id, newScore))
        }
        historyModel.reset()
    }
}

sealed class ScoreModelAction {
    data class UpdateScore(val id: Int, val score: Score) : ScoreModelAction()
    data class NewPlayer(val id: Int, val score: Score) : ScoreModelAction()
}