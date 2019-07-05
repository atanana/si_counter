package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.exceptions.UnknownId
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScoresPresenter(
    private val model: ScoresModel,
    private val playerControlFabric: PlayerControlFabric
) {
    private val scoreViews: MutableMap<Int, PlayerControl> = hashMapOf()
    private val uiScope = MainScope()

    private suspend fun subscribeToPlayersUpdates() = uiScope.launch {
        for ((score, id) in model.updatedPlayersChannel) {
            val playerControl = scoreViews[id] ?: throw UnknownId(id)
            playerControl.update(score)
        }
    }

    private suspend fun subscribeToNewPlayers(scoresContainer: ViewGroup, priceSelector: PriceSelector) =
        uiScope.launch {
            for ((score, id) in model.newPlayersChannel) {
                withContext(Dispatchers.Main) {
                    val playerControl = playerControlFabric.build()
                    playerControl.update(score, id)
                    launch {
                        for (scoreAction in playerControl.scoreActionsChannel) {
                            model.onScoreAction(scoreAction.copy(price = priceSelector.price))
                        }
                    }
                    scoresContainer.addView(playerControl)
                    scoreViews[id] = playerControl
                }
            }
        }

    suspend fun connect(priceSelector: PriceSelector, scoresContainer: ViewGroup) {
        subscribeToNewPlayers(scoresContainer, priceSelector)
        subscribeToPlayersUpdates()
    }
}