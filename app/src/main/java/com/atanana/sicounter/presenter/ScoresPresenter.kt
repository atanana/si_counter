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
import io.reactivex.subjects.PublishSubject

class ScoresPresenter(
    private val model: ScoresModel,
    private val playerControlFabric: PlayerControlFabric
) {
    private val scoreViews: MutableMap<Int, PlayerControl> = hashMapOf()
    private val scoreActions = PublishSubject.create<ScoreAction>()

    private fun subscribeToScoreActions(priceSelector: PriceSelector): Disposable =
        model.subscribeToScoreActions(
            scoreActions.map { action -> action.copy(price = priceSelector.price) }
        )

    private fun subscribeToPlayersUpdates(): Disposable =
        model.updatedPlayersObservable.subscribe { (score, id) ->
            val playerControl = scoreViews[id] ?: throw UnknownId(id)
            playerControl.update(score)
        }

    private fun subscribeToNewPlayers(scoresContainer: ViewGroup): Disposable =
        model.newPlayersObservable.subscribe { (score, id) ->
            val playerControl = playerControlFabric.build()
            playerControl.update(score, id)
            playerControl.scoreActions.subscribe { scoreAction ->
                scoreActions.onNext(scoreAction)
            }
            scoresContainer.addView(playerControl)
            scoreViews[id] = playerControl
        }

    fun connect(priceSelector: PriceSelector, scoresContainer: ViewGroup): Disposable =
        CompositeDisposable().apply {
            addAll(
                subscribeToNewPlayers(scoresContainer),
                subscribeToPlayersUpdates(),
                subscribeToScoreActions(priceSelector)
            )
        }
}