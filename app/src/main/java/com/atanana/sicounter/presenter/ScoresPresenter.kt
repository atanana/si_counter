package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import io.reactivex.subjects.PublishSubject

class ScoresPresenter(
    model: ScoresModel,
    private val scoresContainer: ViewGroup,
    private val playerControlFabric: PlayerControlFabric,
    priceSelector: PriceSelector
) {
    private val scoreViews: MutableMap<Int, PlayerControl> = hashMapOf()
    private val scoreActions = PublishSubject.create<ScoreAction>()

    init {
        model.newPlayersObservable.subscribe({ (score, id) ->
            val playerControl = playerControlFabric.build()
            playerControl.update(score, id)
            playerControl.scoreActions.subscribe({ scoreAction ->
                scoreActions.onNext(scoreAction)
            })
            scoresContainer.addView(playerControl)
            scoreViews[id] = playerControl
        })

        model.updatedPlayersObservable.subscribe({ (score, id) ->
            val playerControl = scoreViews[id] ?: throw UnknownId(id)
            playerControl.update(score)
        })

        model.subscribeToScoreActions(
            scoreActions.map { action -> action.copy(price = priceSelector.price) }
        )
    }
}