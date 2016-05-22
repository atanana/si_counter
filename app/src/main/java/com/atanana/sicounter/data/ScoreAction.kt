package com.atanana.sicounter.data

import com.atanana.sicounter.exceptions.SiCounterException

data class ScoreAction(val type: ScoreActionType, val price: Int?, val id: Int) {
    constructor(type: ScoreActionType, id: Int) : this(type, null, id)

    val absolutePrice: Int
        get() {
            val safePrice = price ?: throw SiCounterException("No price defined!")
            return when (type) {
                ScoreActionType.PLUS -> safePrice
                ScoreActionType.MINUS -> -safePrice
            }
        }
}