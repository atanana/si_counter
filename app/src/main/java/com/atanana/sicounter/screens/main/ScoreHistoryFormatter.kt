package com.atanana.sicounter.screens.main

import android.content.Context
import com.atanana.sicounter.R
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType

open class ScoreHistoryFormatter(context: Context) {
    private val newPlayerTemplate by lazy { context.resources.getString(R.string.new_player_log) }
    open val resetMessage by lazy { context.resources.getText(R.string.reset_log).toString() + "\n" }

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