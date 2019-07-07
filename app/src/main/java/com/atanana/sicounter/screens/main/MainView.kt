package com.atanana.sicounter.screens.main

import com.atanana.sicounter.view.player_control.PlayerControl
import kotlinx.coroutines.CoroutineScope

interface MainView {
    val uiScope: CoroutineScope

    val selectedPrice: Int

    fun showAddPlayerDialog()

    fun showResetDialog()

    fun showQuitDialog()

    fun appendLogs(line: String)

    fun addPlayerControl(playerControl: PlayerControl)
}