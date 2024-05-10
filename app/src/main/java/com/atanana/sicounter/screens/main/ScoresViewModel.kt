package com.atanana.sicounter.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atanana.sicounter.data.NoAnswer
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.model.ScoresModel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ScoresViewModel(
    private val model: ScoresModel,
) : ViewModel() {

    val actions = model.actionsChannel.receiveAsFlow()

    fun onNoAnswer(currentPrice: Int) {
        viewModelScope.launch {
            model.onScoreAction(NoAnswer(currentPrice))
        }
    }

    fun onScoreAction(action: ScoreAction) {
        viewModelScope.launch {
            model.onScoreAction(action)
        }
    }
}
