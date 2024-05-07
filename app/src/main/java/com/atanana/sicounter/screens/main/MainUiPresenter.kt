package com.atanana.sicounter.screens.main

import android.os.Bundle
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel

class MainUiPresenter(
    private val scoresModel: ScoresModel,
    private val historyModel: HistoryModel
) {

    fun saveToBundle(outState: Bundle) {
        scoresModel.save(outState)
        historyModel.save(outState)
    }

    suspend fun restoreFromBundle(savedInstanceState: Bundle?) {
        scoresModel.restore(savedInstanceState)
        historyModel.restore(savedInstanceState)
    }
}
