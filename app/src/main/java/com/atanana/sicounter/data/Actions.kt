package com.atanana.sicounter.data

data class PartialScoreAction(val type: ScoreActionType, val id: Int)

sealed class ScoreAction {
    abstract val price: Int
}

data class NoAnswer(
    override val price: Int
) : ScoreAction()

data class ScoreChange(
    val type: ScoreActionType,
    val id: Int,
    override val price: Int
) : ScoreAction() {

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