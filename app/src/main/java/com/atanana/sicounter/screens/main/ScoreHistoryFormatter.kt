package com.atanana.sicounter.screens.main

import android.content.Context
import com.atanana.sicounter.R
import com.atanana.sicounter.data.NoAnswer
import com.atanana.sicounter.data.ScoreChange

class ScoreHistoryFormatter(context: Context) {
    private val newPlayerTemplate by lazy { context.resources.getString(R.string.new_player_log) }
    val resetMessage by lazy { context.resources.getText(R.string.reset_log).toString() + "\n" }
    private val noAnswer by lazy { context.resources.getString(R.string.no_answer_log) }

    fun formatScoreChange(scoreChange: ScoreChange, playerName: String): String =
        "$playerName ${scoreChange.absolutePrice}"

    fun formatNoAnswer(action: NoAnswer): String = String.format(noAnswer, action.price)

    fun formatNewPlayer(playerName: String): String {
        return String.format(newPlayerTemplate, playerName)
    }
}