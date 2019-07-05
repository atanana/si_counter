package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class ScoresPresenter(
    private val model: ScoresModel,
    private val playerControlFabric: PlayerControlFabric
) {
    private val scoreViews: MutableMap<Int, PlayerControl> = hashMapOf()

    private fun subscribeToPlayersUpdates(): Disposable =
        model.updatedPlayersObservable.subscribe { (score, id) ->
            val playerControl = scoreViews[id] ?: throw UnknownId(id)
            playerControl.update(score)
        }

    private suspend fun subscribeToNewPlayers(scoresContainer: ViewGroup, priceSelector: PriceSelector) =
        withContext(Dispatchers.Main) {
            for ((score, id) in model.newPlayersChannel) {
                val playerControl = playerControlFabric.build()
                playerControl.update(score, id)
                playerControl.scoreActions.subscribe { scoreAction ->
                    model.onScoreAction(scoreAction.copy(price = priceSelector.price))
                }
                scoresContainer.addView(playerControl)
                scoreViews[id] = playerControl
            }
        }

    suspend fun connect(priceSelector: PriceSelector, scoresContainer: ViewGroup): Disposable {
        subscribeToNewPlayers(scoresContainer, priceSelector)
        return CompositeDisposable().apply {
            addAll(
                subscribeToPlayersUpdates()
            )
        }
    }
}