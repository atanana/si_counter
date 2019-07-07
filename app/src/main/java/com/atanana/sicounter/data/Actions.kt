package com.atanana.sicounter.data

data class PartialScoreAction(val type: ScoreActionType, val id: Int)

data class ScoreAction(
    val type: ScoreActionType,
    val id: Int,
    val price: Int
) {
    constructor(action: PartialScoreAction, price: Int) : this(action.type, action.id, price)

    val absolutePrice: Int
        get() = when (type) {
            ScoreActionType.PLUS -> price
            ScoreActionType.MINUS -> -price
        }
}

enum class ScoreActionType {
    PLUS,
    MINUS;
}