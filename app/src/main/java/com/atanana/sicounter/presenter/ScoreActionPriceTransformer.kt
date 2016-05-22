package com.atanana.sicounter.presenter

import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.view.PriceSelector
import rx.Observable

object ScoreActionPriceTransformer {
    fun transform(actions: Observable<ScoreAction>, priceSelector: PriceSelector): Observable<ScoreAction> {
        return actions.map { action -> action.copy(price = priceSelector.price) }
    }
}