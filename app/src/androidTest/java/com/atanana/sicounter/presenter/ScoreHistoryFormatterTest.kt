package com.atanana.sicounter.presenter

import android.test.AndroidTestCase
import com.atanana.sicounter.R
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS

class ScoreHistoryFormatterTest : AndroidTestCase() {
    fun testNewPlayerFormat() {
        val result = ScoreHistoryFormatter(context).formatNewPlayer("test 1")
        val template = context.resources.getText(R.string.new_player_log).toString()
        assertEquals(String.format(template, "test 1"), result)
    }

    fun testPositiveScoreAction() {
        val result = ScoreHistoryFormatter(context).formatScoreAction(ScoreAction(PLUS, 20, 0), "test 1")
        assertEquals("test 1 +20", result)
    }

    fun testNegativeScoreAction() {
        val result = ScoreHistoryFormatter(context).formatScoreAction(ScoreAction(MINUS, 20, 0), "test 1")
        assertEquals("test 1 -20", result)
    }
}