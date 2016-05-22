package com.atanana.sicounter.presenter

import android.test.AndroidTestCase
import android.view.ViewGroup
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.data.ScoreActionType
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import org.mockito.Mockito.*
import rx.Observable.just
import rx.observers.TestSubscriber

class ScoresPresenterTest : AndroidTestCase() {
    fun testScoreActions() {
        val model = mock(ScoresModel::class.java)
        `when`(model.newPlayers).thenReturn(just(
                Pair(Score("test 1", 0), 123),
                Pair(Score("test 2", 0), 124)
        ))
        val container = mock(ViewGroup::class.java)
        val fabric = mock(PlayerControlFabric::class.java)
        val playerControl1 = PlayerControl(context, null)
        val playerControl2 = PlayerControl(context, null)
        `when`(fabric.build()).thenReturn(playerControl1, playerControl2)
        val priceSelector = mock(PriceSelector::class.java)
        `when`(priceSelector.price).thenReturn(30, 40)
        val presenter = ScoresPresenter(model, container, fabric, priceSelector)

        val subscriber = TestSubscriber<ScoreAction>()
        presenter.scoreActions.subscribe(subscriber)

        playerControl1.findViewById(R.id.add_score).performClick()
        playerControl2.findViewById(R.id.subtract_score).performClick()

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(
                ScoreAction(ScoreActionType.PLUS, 30, 123),
                ScoreAction(ScoreActionType.MINUS, 40, 124)
        ))
    }
}