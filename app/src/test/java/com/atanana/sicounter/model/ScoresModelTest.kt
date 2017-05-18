package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import rx.Observable.just
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber
import rx.subjects.Subject

private fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}

class ScoresModelTest {
    @Mock
    lateinit var formatter: ScoreHistoryFormatter

    lateinit var newPlayers: Subject<String, String>

    lateinit var model: ScoresModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        newPlayers = PublishSubject<String>()
        model = ScoresModel(newPlayers, formatter)
    }

    @Test
    fun shouldNotifyAboutNewPlayersScores() {
        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.newPlayersObservable.subscribe(subscriber)

        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        subscriber.assertValues(Pair(Score("test 1", 0), 0), Pair(Score("test 2", 0), 1))
    }

    @Test
    fun shouldNotifyAboutNewPlayersHistory() {
        val subscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(subscriber)

        `when`(formatter.formatNewPlayer("test 1")).thenReturn("player 1")
        `when`(formatter.formatNewPlayer("test 2")).thenReturn("player 2")

        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        subscriber.assertValues("player 1", "player 2")
    }

    @Test
    fun shouldWriteNewPlayersToHistory() {
        `when`(formatter.formatNewPlayer("test 1")).thenReturn("player 1")
        `when`(formatter.formatNewPlayer("test 2")).thenReturn("player 2")

        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        assertThat(model.history).isEqualTo(listOf("player 1", "player 2"))
    }

    @Test
    fun shouldNotifyAboutScoresUpdate() {
        model = ScoresModel(just("test 1", "test 2"), formatter)

        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.updatedPlayersObservable.subscribe(subscriber)

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        subscriber.assertValues(
                Pair(Score("test 1", 10), 0),
                Pair(Score("test 1", 30), 0),
                Pair(Score("test 2", -20), 1)
        )
    }

    @Test
    fun shouldNotifyAboutUpdateScoresHistory() {
        model = ScoresModel(just("test 1", "test 2"), formatter)

        val subscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(subscriber)

        `when`(formatter.formatScoreAction(anyObject(), anyObject())).thenAnswer {
            val action: ScoreAction = it.arguments[0] as ScoreAction
            val name: String = it.arguments[1] as String
            return@thenAnswer action.absolutePrice.toString() + name
        }

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        subscriber.assertValues("10test 1", "20test 1", "-20test 2")
    }

    @Test
    fun shouldWriteUpdateScoresToHistory() {
        model = ScoresModel(just("test 1", "test 2"), formatter)

        `when`(formatter.formatScoreAction(anyObject(), anyObject())).thenAnswer {
            val action: ScoreAction = it.arguments[0] as ScoreAction
            val name: String = it.arguments[1] as String
            return@thenAnswer action.absolutePrice.toString() + name
        }

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        assertThat(model.history).contains("10test 1", "20test 1", "-20test 2")
    }

    @Test
    fun shouldNotifyAboutScoresReset() {
        model = ScoresModel(just("test 1", "test 2"), formatter)

        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.updatedPlayersObservable.subscribe(subscriber)

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))
        model.reset()

        assertThat(subscriber.onNextEvents).endsWith(Pair(Score("test 1", 0), 0), Pair(Score("test 2", 0), 1))
    }

    @Test
    fun shouldNotifyAboutResetScoresHistory() {
        val subscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(subscriber)
        `when`(formatter.resetMessage).thenReturn("reset message")

        model.reset()

        subscriber.assertValue("reset message")
    }

    @Test
    fun shouldWriteResetScoresToHistory() {
        `when`(formatter.resetMessage).thenReturn("reset message")
        model.reset()
        assertThat(model.history).isEqualTo(listOf("reset message"))
    }
}