package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PlayerControl

class ScoresPresenter(private val model: ScoresModel, private val scoresContainer: ViewGroup) {
    init {
        model.newPlayers.subscribe({ newPlayer ->
            val playerControl = PlayerControl(scoresContainer.context, null)
            playerControl.update(newPlayer)
            scoresContainer.addView(playerControl)
        })
    }
}