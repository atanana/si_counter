package com.atanana.sicounter.presenter

import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.data.ScoreActionType
import com.atanana.sicounter.presenter.ScoreActionPriceTransformer.transform
import com.atanana.sicounter.view.PriceSelector
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import rx.Observable
import rx.observers.TestSubscriber

class ScoreActionPriceTransformerTest {

    @Test
    fun testTransform() {
        val actions = Observable.just(
                ScoreAction(ScoreActionType.PLUS, 123), ScoreAction(ScoreActionType.PLUS, 123)
        )
        val priceSelector = mock(PriceSelector::class.java)
        `when`(priceSelector.price).thenReturn(20, 30)
        val subscriber = TestSubscriber<ScoreAction>()

        transform(actions, priceSelector).subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(
                ScoreAction(ScoreActionType.PLUS, 20, 123),
                ScoreAction(ScoreActionType.PLUS, 30, 123)
        ))
    }
}