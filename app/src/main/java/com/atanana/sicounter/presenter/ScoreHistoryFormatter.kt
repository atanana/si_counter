package com.atanana.sicounter.presenter

import android.content.Context
import com.atanana.sicounter.R
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType

open class ScoreHistoryFormatter(context: Context) {
    private val newPlayerTemplate: String = context.resources.getText(R.string.new_player_log).toString()
    val resetMessage: String = context.resources.getText(R.string.reset_log).toString() + "\n"

    open fun formatScoreAction(scoreAction: ScoreAction, playerName: String): String {
        val sign: String = when (scoreAction.type) {
            ScoreActionType.PLUS -> "+"
            ScoreActionType.MINUS -> "-"
        }
        return "$playerName $sign${scoreAction.price}"
    }

    open fun formatNewPlayer(playerName: String): String {
        return String.format(newPlayerTemplate, playerName)
    }
}