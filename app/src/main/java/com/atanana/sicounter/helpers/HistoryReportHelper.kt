package com.atanana.sicounter.helpers

import com.atanana.sicounter.model.HISTORY_SEPARATOR
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel

class HistoryReportHelper(
    private val historyModel: HistoryModel,
    private val scoresModel: ScoresModel
) {
    fun createReport(): List<String> {
        var report = historyModel.history
        report += HISTORY_SEPARATOR
        report += scoresModel.scores
            .map { "${it.name} -> ${it.score}" }
            .joinToString(", ")
        return report
    }
}