package com.atanana.sicounter.data

import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import com.atanana.sicounter.exceptions.SiCounterException
import org.junit.Test
import org.junit.Assert.*

class ScoreActionTest {
    @Test
    fun absolutePricePlus() {
        val action = ScoreAction(ScoreActionType.PLUS, 20, 0)
        assertEquals(20, action.absolutePrice)
    }

    @Test
    fun absolutePriceMinus() {
        val action = ScoreAction(ScoreActionType.MINUS, 20, 0)
        assertEquals(-20, action.absolutePrice)
    }

    @Test(expected = SiCounterException::class)
    fun absolutePriceThrowsAnError() {
        val action = ScoreAction(ScoreActionType.PLUS, 0)
        assertEquals(20, action.absolutePrice)
    }
}