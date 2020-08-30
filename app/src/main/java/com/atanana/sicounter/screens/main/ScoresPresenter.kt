package com.atanana.sicounter.screens.main

import com.atanana.sicounter.UnknownId
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.model.ScoreModelAction.*
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ScoresPresenter(
    private val view: MainView,
    private val model: ScoresModel,
    private val playerControlFabric: PlayerControlFabric
) {
    private val scoreViews: MutableMap<Int, PlayerControl> = hashMapOf()

    fun connect(scope: CoroutineScope) {
        scope.launch {
            for (action in model.actionsChannel) {
                when (action) {
                    is UpdateScore -> handleScoreUpdate(action)
                    is NewPlayer -> handleNewPlayer(action, scope)
                    is SetPrice -> handleNextPrice(action)
                }
            }
        }
    }

    private fun handleScoreUpdate(action: UpdateScore) {
        val (id, score) = action
        val playerControl = scoreViews[id] ?: throw UnknownId(id)
        playerControl.update(score)
    }

    private fun handleNewPlayer(action: NewPlayer, scope: CoroutineScope) {
        val (id, score) = action
        val playerControl = createPlayerControl(score, id, scope)
        view.addPlayerControl(playerControl)
        scoreViews[id] = playerControl
    }

    private fun createPlayerControl(score: Score, id: Int, scope: CoroutineScope): PlayerControl {
        val playerControl = playerControlFabric.build()
        playerControl.update(score, id)
        scope.launch {
            for (scoreAction in playerControl.scoreActionsChannel) {
                model.onScoreAction(ScoreAction(scoreAction, view.selectedPrice))
            }
        }
        return playerControl
    }

    private fun handleNextPrice(action: SetPrice) {
        view.selectedPrice = action.price
    }
}