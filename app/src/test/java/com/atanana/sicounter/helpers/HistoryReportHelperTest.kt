package com.atanana.sicounter.helpers

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.model.HISTORY_SEPARATOR
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import io.kotlintest.matchers.collections.shouldStartWith
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk

class HistoryReportHelperTest : StringSpec({
    val scoresModel: ScoresModel = mockk {
        every { scores } returns emptyList()
    }

    val historyModel: HistoryModel = mockk {
        every { history } returns emptyList()
    }

    val helper = HistoryReportHelper(historyModel, scoresModel)

    "should put history in report" {
        every { historyModel.history } returns listOf("test 1", "test 2", "test 3")
        helper.createReport() shouldStartWith listOf("test 1", "test 2", "test 3")
    }

    "should add separator" {
        every { historyModel.history } returns listOf("test 1", "test 2", "test 3")
        val report = helper.createReport()
        report[report.size - 2] shouldBe HISTORY_SEPARATOR
    }

    "should add total section" {
        every { scoresModel.scores } returns listOf(
            Score("test 1", -10),
            Score("test 2", 20),
            Score("test 3", 40)
        )
        helper.createReport().last() shouldBe "test 1 -> -10, test 2 -> 20, test 3 -> 40"
    }
})