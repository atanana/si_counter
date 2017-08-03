package com.atanana.sicounter.helpers

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.model.HISTORY_SEPARATOR
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import org.mockito.Mockito.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HistoryReportHelperTest {
    lateinit var helper:HistoryReportHelper

    @Mock
    lateinit var scoresModel:ScoresModel

    @Mock
    lateinit var historyModel:HistoryModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        helper = HistoryReportHelper(historyModel, scoresModel)

        `when`(historyModel.history).thenReturn(emptyList())
        `when`(scoresModel.scores).thenReturn(emptyList())
    }

    @Test
    fun shouldPutHistoryInReport() {
        `when`(historyModel.history).thenReturn(listOf("test 1", "test 2", "test 3"))
        assertThat(helper.createReport()).startsWith("test 1", "test 2", "test 3")
    }

    @Test
    fun shouldAddSeparator() {
        `when`(historyModel.history).thenReturn(listOf("test 1", "test 2", "test 3"))
        val report = helper.createReport()
        assertThat(report[report.size - 2]).isEqualTo(HISTORY_SEPARATOR)
    }

    @Test
    fun shouldAddTotalSection() {
        `when`(scoresModel.scores).thenReturn(listOf(
                Score("test 1", -10),
                Score("test 2", 20),
                Score("test 3", 40)
        ))
        assertThat(helper.createReport().last()).isEqualTo("test 1 -> -10, test 2 -> 20, test 3 -> 40")
    }
}