package com.atanana.sicounter.presenter

import android.content.Context
import android.content.res.Resources
import com.atanana.sicounter.R
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class ScoreHistoryFormatterTest {
    private lateinit var formatter: ScoreHistoryFormatter

    @Mock
    lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val resources = mock(Resources::class.java)
        `when`(context.resources).thenReturn(resources)

        formatter = ScoreHistoryFormatter(context)
    }

    @Test
    fun shouldProperlyFormatNewPlayer() {
        `when`(context.resources.getString(R.string.new_player_log)).thenReturn("new player %s")
        assertThat(formatter.formatNewPlayer("test")).isEqualTo("new player test")
    }

    @Test
    fun shouldProperlyFormatScoreAction() {
        assertThat(formatter.formatScoreAction(ScoreAction(ScoreActionType.PLUS, 10, 1), "test 1")).isEqualTo("test 1 +10")
        assertThat(formatter.formatScoreAction(ScoreAction(ScoreActionType.MINUS, 30, 2), "test 2")).isEqualTo("test 2 -30")
    }
}