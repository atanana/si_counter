package com.atanana.sicounter.screens.main

import android.view.ViewGroup
import com.atanana.sicounter.exceptions.UnknownId
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ScoresPresenter(
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

    private fun CoroutineScope.subscribeToNewPlayers(
        scoresContainer: ViewGroup,
        priceSelector: PriceSelector
    ) {
        launch {
            for ((score, id) in model.newPlayersChannel) {
                val playerControl = playerControlFabric.build()
                playerControl.update(score, id)
                subscribeToScoreActions(playerControl, priceSelector)
                scoresContainer.addView(playerControl)
                scoreViews[id] = playerControl
            }
        }
    }

    private fun CoroutineScope.subscribeToScoreActions(
        playerControl: PlayerControl,
        priceSelector: PriceSelector
    ) {
        launch {
            for (scoreAction in playerControl.scoreActionsChannel) {
                model.onScoreAction(scoreAction.copy(price = priceSelector.price))
            }
        }
    }

    fun connect(
        scope: CoroutineScope,
        priceSelector: PriceSelector,
        scoresContainer: ViewGroup
    ) {
        scope.subscribeToNewPlayers(scoresContainer, priceSelector)
        scope.subscribeToPlayersUpdates()
    }
}