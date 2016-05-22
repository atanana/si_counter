package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.data.ScoreActionType
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class ScoresModelTest {
    @Test
    fun newPlayers() {
        val newPlayer = PublishSubject<String>()
        val model = ScoresModel(newPlayer)
        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.newPlayers.subscribe(subscriber)

        newPlayer.onNext("test 1")
        newPlayer.onNext("test 2")

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(Pair(Score("test 1", 0), 0), Pair(Score("test 2", 0), 1)))
    }

    @Test
    fun updatePlayers() {
        val model = ScoresModel(just("test 1", "test 2"))
        val subscriber = TestSubscriber<Pair<Score, Int>>()
        model.updatedPlayers.subscribe(subscriber)

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
    }
}