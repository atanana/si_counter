package com.atanana.sicounter.model

import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.observers.TestSubscriber

private fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}

class HistoryModelTest {
    @Mock
    lateinit var formatter: ScoreHistoryFormatter

    lateinit var model: HistoryModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        model = HistoryModel(formatter)
    }

    @Test
    fun shouldNotifyAboutNewPlayersHistory() {
        val subscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(subscriber)

        `when`(formatter.formatNewPlayer("test 1")).thenReturn("player 1")
        `when`(formatter.formatNewPlayer("test 2")).thenReturn("player 2")

        model.onPlayerAdded("test 1")
        model.onPlayerAdded("test 2")

        subscriber.assertValues("player 1", "player 2")
    }

    @Test
    fun shouldWriteNewPlayersToHistory() {
        `when`(formatter.formatNewPlayer("test 1")).thenReturn("player 1")
        `when`(formatter.formatNewPlayer("test 2")).thenReturn("player 2")

        model.onPlayerAdded("test 1")
        model.onPlayerAdded("test 2")

        assertThat(model.history).isEqualTo(listOf("player 1", "player 2"))
    }

    @Test
    fun shouldNotifyAboutUpdateScoresHistory() {
        val subscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(subscriber)

        `when`(formatter.formatScoreAction(anyObject(), anyObject())).thenAnswer {
            val action: ScoreAction = it.arguments[0] as ScoreAction
            val name: String = it.arguments[1] as String
            return@thenAnswer action.absolutePrice.toString() + name
        }

        model.onScoreAction(ScoreAction(ScoreActionType.PLUS, 10, 0), "test 1")
        model.onScoreAction(ScoreAction(ScoreActionType.PLUS, 20, 0), "test 1")
        model.onScoreAction(ScoreAction(ScoreActionType.MINUS, 20, 1), "test 2")

        subscriber.assertValues("10test 1", "20test 1", "-20test 2")
    }

    @Test
    fun shouldWriteUpdateScoresToHistory() {
        `when`(formatter.formatScoreAction(anyObject(), anyObject())).thenAnswer {
            val action: ScoreAction = it.arguments[0] as ScoreAction
            val name: String = it.arguments[1] as String
            return@thenAnswer action.absolutePrice.toString() + name
        }

        model.onScoreAction(ScoreAction(ScoreActionType.PLUS, 10, 0), "test 1")
        model.onScoreAction(ScoreAction(ScoreActionType.PLUS, 20, 0), "test 1")
        model.onScoreAction(ScoreAction(ScoreActionType.MINUS, 20, 1), "test 2")

        assertThat(model.history).contains("10test 1", "20test 1", "-20test 2")
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