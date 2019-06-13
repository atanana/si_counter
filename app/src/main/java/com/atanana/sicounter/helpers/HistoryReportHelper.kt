package com.atanana.sicounter.helpers

import com.atanana.sicounter.model.HISTORY_SEPARATOR
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel

class HistoryReportHelper(
    private val historyModel: HistoryModel,
    private val scoresModel: ScoresModel
) {
    fun createReport(): List<String> {
        val report = historyModel.history.toMutableList()
        report.add(HISTORY_SEPARATOR)
        report.add(scoresModel.scores.joinToString(", ") { "${it.name} -> ${it.score}" })
        return report
    }
}