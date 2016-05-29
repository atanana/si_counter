package com.atanana.sicounter.model

import android.test.AndroidTestCase
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import rx.Observable.just
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class ScoresModelTest : AndroidTestCase() {
    lateinit var formatter: ScoreHistoryFormatter

    override fun setUp() {
        super.setUp()
        formatter = ScoreHistoryFormatter(context)
    }

    fun testNewPlayers() {
        val newPlayer = PublishSubject<String>()
        val model = ScoresModel(newPlayer, formatter)
        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.newPlayers.subscribe(subscriber)
        val historySubscriber = TestSubscriber<String>()
        model.historyChanges.subscribe(historySubscriber)

        newPlayer.onNext("test 1")
        newPlayer.onNext("test 2")

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(Pair(Score("test 1", 0), 0), Pair(Score("test 2", 0), 1)))
        historySubscriber.assertNoErrors()
        historySubscriber.assertReceivedOnNext(mutableListOf(
                formatter.formatNewPlayer("test 1"),
                formatter.formatNewPlayer("test 2")
        ))
    }

    fun testUpdatePlayers() {
        val model = ScoresModel(just("test 1", "test 2"), formatter)
        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.updatedPlayers.subscribe(subscriber)
        val historySubscriber = TestSubscriber<String>()
        model.historyChanges.subscribe(historySubscriber)

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(ScoreActionType.MINUS, 20, 1)
        ))

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(
                Pair(Score("test 1", 10), 0),
                Pair(Score("test 1", 30), 0),
                Pair(Score("test 2", -20), 1)
        ))
        historySubscriber.assertNoErrors()
        historySubscriber.assertReceivedOnNext(mutableListOf(
                formatter.formatScoreAction(ScoreAction(PLUS, 10, 0), "test 1"),
                formatter.formatScoreAction(ScoreAction(PLUS, 20, 0), "test 1"),
                formatter.formatScoreAction(ScoreAction(ScoreActionType.MINUS, 20, 1), "test 2")
        ))
    }

    fun testReset() {
        val model = ScoresModel(just("test 1", "test 2"), formatter)
        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 50, 1)
        ))
        val playersSubscriber = TestSubscriber<Pair<Score, Int>>()
        model.updatedPlayers.subscribe(playersSubscriber)
        val historySubscriber = TestSubscriber<String>()
        model.historyChanges.subscribe(historySubscriber)

        model.reset()

        playersSubscriber.assertNoErrors()
        assertThat(playersSubscriber.onNextEvents, Matchers.containsInAnyOrder(
                Pair(Score("test 1", 0), 0),
                Pair(Score("test 2", 0), 1)
        ))
        historySubscriber.assertNoErrors()
        historySubscriber.assertReceivedOnNext(listOf(
                formatter.resetMessage
        ))
    }
}