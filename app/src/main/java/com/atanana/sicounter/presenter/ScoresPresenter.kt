package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

class ScoresPresenter(private val model: ScoresModel,
                      private val scoresContainer: ViewGroup,
                      private val playerControlFabric: PlayerControlFabric,
                      private val priceSelector: PriceSelector) {
    private val _scoreActions: Subject<ScoreAction, ScoreAction> = PublishSubject()
    val scoreActions: Observable<ScoreAction>
        get() = _scoreActions

    init {
        model.newPlayers.subscribe({ newPlayer ->
            val playerControl = playerControlFabric.build()
            playerControl.update(newPlayer.first, newPlayer.second)
            playerControl.scoreActions.subscribe({ scoreAction ->
                _scoreActions.onNext(scoreAction.copy(price = priceSelector.price))
            })
            scoresContainer.addView(playerControl)
        })
    }
}