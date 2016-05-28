package com.atanana.sicounter.presenter

import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.data.ScoreActionType
import com.atanana.sicounter.model.ScoresModel

object ScoreActionFormatter {
    fun format(scoreAction: ScoreAction, scoresModel: ScoresModel): String {
        val name = scoresModel.playerNameById(scoreAction.id)
        val sign: String = when (scoreAction.type) {
            ScoreActionType.PLUS -> "+"
            ScoreActionType.MINUS -> "-"
        }
        return "$name $sign${scoreAction.price}"
    }
}