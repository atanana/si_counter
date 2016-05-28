package com.atanana.sicounter.model

import android.test.AndroidTestCase
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import rx.Observable.just
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class ScoresModelTest : AndroidTestCase() {
    fun testNewPlayers() {
        val newPlayer = PublishSubject<String>()
        val formatter = ScoreHistoryFormatter(context)
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
        val formatter = ScoreHistoryFormatter(context)
        val model = ScoresModel(just("test 1", "test 2"), formatter)
        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.updatedPlayers.subscribe(subscriber)
        val historySubscriber = TestSubscriber<String>()
        model.historyChanges.subscribe(historySubscriber)

        model.subscribeToScoreActions(just(
                ScoreAction(ScoreActionType.PLUS, 10, 0),
                ScoreAction(ScoreActionType.PLUS, 20, 0),
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
                formatter.formatScoreAction(ScoreAction(ScoreActionType.PLUS, 10, 0), "test 1"),
                formatter.formatScoreAction(ScoreAction(ScoreActionType.PLUS, 20, 0), "test 1"),
                formatter.formatScoreAction(ScoreAction(ScoreActionType.MINUS, 20, 1), "test 2")
        ))
    }
}