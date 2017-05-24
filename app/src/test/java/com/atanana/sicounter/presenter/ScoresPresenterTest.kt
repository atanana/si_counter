package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject

private fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}

class ScoresPresenterTest {
    private lateinit var presenter: ScoresPresenter

    @Mock
    lateinit var model: ScoresModel

    @Mock
    lateinit var container: ViewGroup

    @Mock
    lateinit var fabric: PlayerControlFabric

    @Mock
    lateinit var priceSelector: PriceSelector

    lateinit var newPlayers: PublishSubject<Pair<Score, Int>>
    lateinit var updatedPlayers: PublishSubject<Pair<Score, Int>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        newPlayers = PublishSubject()
        `when`(model.newPlayersObservable).thenReturn(newPlayers)
        updatedPlayers = PublishSubject()
        `when`(model.updatedPlayersObservable).thenReturn(updatedPlayers)
    }

    @Test
    fun shouldCreatePlayerControls() {
        val playerControl = mockPlayerControl()
        `when`(fabric.build()).thenReturn(playerControl)

        createPresenter()
        newPlayers.onNext(Pair(Score("test 1", 0), 1))

        verify(container).addView(playerControl)
    }

    @Test
    fun shouldCreatePlayerControlWithValidData() {
        val playerControl = mockPlayerControl()
        `when`(fabric.build()).thenReturn(playerControl)

        createPresenter()
        val score = Score("test 1", 0)
        newPlayers.onNext(Pair(score, 1))

        verify(playerControl).update(score, 1)
    }

    @Test
    fun shouldTransferScoreActionsFromPlayerControls() {
        val playerControl1 = mockPlayerControl()
        val scoreActions1 = PublishSubject<ScoreAction>()
        `when`(playerControl1.scoreActions).thenReturn(scoreActions1)

        val playerControl2 = mockPlayerControl()
        val scoreActions2 = PublishSubject<ScoreAction>()
        `when`(playerControl2.scoreActions).thenReturn(scoreActions2)

        `when`(fabric.build()).thenReturn(playerControl1, playerControl2)

        val subscriber = TestSubscriber<ScoreAction>()
        `when`(model.subscribeToScoreActions(anyObject())).thenAnswer {
            @Suppress("UNCHECKED_CAST")
            val observable = it.arguments[0] as Observable<ScoreAction>
            observable.subscribe(subscriber)
        }

        createPresenter()
        newPlayers.onNext(Pair(Score("test 1", 0), 1))
        newPlayers.onNext(Pair(Score("test 2", 0), 2))

        `when`(priceSelector.price).thenReturn(20)
        scoreActions1.onNext(ScoreAction(ScoreActionType.PLUS, 1))
        subscriber.assertValue(ScoreAction(ScoreActionType.PLUS, 20, 1))

        `when`(priceSelector.price).thenReturn(40)
        scoreActions1.onNext(ScoreAction(ScoreActionType.MINUS, 2))
        subscriber.assertValues(ScoreAction(ScoreActionType.PLUS, 20, 1), ScoreAction(ScoreActionType.MINUS, 40, 2))
    }

    @Test
    fun shouldUpdateCorrectPlayerControl() {
        val playerControl1 = mockPlayerControl()
        val playerControl2 = mockPlayerControl()
        `when`(fabric.build()).thenReturn(playerControl1, playerControl2)

        createPresenter()
        newPlayers.onNext(Pair(Score("test 1", 0), 1))
        newPlayers.onNext(Pair(Score("test 2", 0), 2))

        reset(playerControl2)
        val score = Score("test 2", 100)
        updatedPlayers.onNext(Pair(score, 2))

        verify(playerControl2).update(score, null)
    }

    private fun createPresenter() {
        presenter = ScoresPresenter(model, container, fabric, priceSelector)
    }

    private fun mockPlayerControl(): PlayerControl {
        val playerControl = mock(PlayerControl::class.java)
        `when`(playerControl.scoreActions).thenReturn(Observable.empty())
        return playerControl
    }
}