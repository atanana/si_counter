package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import org.junit.Test
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class ScoresModelTest {
    @Test
    fun newPlayers() {
        val newPlayer = PublishSubject<String>()
        val model = ScoresModel(newPlayer)
        val subscriber = TestSubscriber<Score>()
        model.newPlayers.subscribe(subscriber)

        newPlayer.onNext("test 1")
        newPlayer.onNext("test 2")

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(Score("test 1", 0), Score("test 2", 0)))
    }
}