package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.screens.main.ScoresPresenter
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControl
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

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

    private lateinit var newPlayers: PublishSubject<Pair<Score, Int>>
    private lateinit var updatedPlayers: PublishSubject<Pair<Score, Int>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        newPlayers = PublishSubject.create()
        `when`(model.newPlayersObservable).thenReturn(newPlayers)
        updatedPlayers = PublishSubject.create()
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
        val scoreActions1 = PublishSubject.create<ScoreAction>()
        `when`(playerControl1.scoreActionsChannel).thenReturn(scoreActions1)

        val playerControl2 = mockPlayerControl()
        val scoreActions2 = PublishSubject.create<ScoreAction>()
        `when`(playerControl2.scoreActionsChannel).thenReturn(scoreActions2)

        `when`(fabric.build()).thenReturn(playerControl1, playerControl2)

        val subscriber = TestObserver<ScoreAction>()
        `when`(model.onScoreAction(anyObject())).thenAnswer {
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
        presenter = ScoresPresenter(model, fabric)
    }

    private fun mockPlayerControl(): PlayerControl {
        val playerControl = mock(PlayerControl::class.java)
        `when`(playerControl.scoreActionsChannel).thenReturn(Observable.empty())
        return playerControl
    }
}