package com.atanana.sicounter.screens.main

import com.atanana.sicounter.exceptions.UnknownId
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

    private fun CoroutineScope.subscribeToPlayersUpdates() {
        launch {
            for ((score, id) in model.updatedPlayersChannel) {
                val playerControl = scoreViews[id] ?: throw UnknownId(id)
                playerControl.update(score)
            }
        }
    }

    private fun CoroutineScope.subscribeToNewPlayers() {
        launch {
            for ((score, id) in model.newPlayersChannel) {
                val playerControl = playerControlFabric.build()
                playerControl.update(score, id)
                subscribeToScoreActions(playerControl)
                view.addPlayerControl(playerControl)
                scoreViews[id] = playerControl
            }
        }
    }

    private fun CoroutineScope.subscribeToScoreActions(playerControl: PlayerControl) {
        launch {
            for (scoreAction in playerControl.scoreActionsChannel) {
                model.onScoreAction(scoreAction.copy(price = view.selectedPrice))
            }
        }
    }

    fun connect(scope: CoroutineScope) {
        scope.subscribeToNewPlayers()
        scope.subscribeToPlayersUpdates()
    }
}