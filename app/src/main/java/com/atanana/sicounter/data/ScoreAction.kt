package com.atanana.sicounter.data

data class ScoreAction(val type: ScoreActionType, val price: Int?, val id: Int) {
    constructor(type: ScoreActionType, id: Int) : this(type, null, id)
}