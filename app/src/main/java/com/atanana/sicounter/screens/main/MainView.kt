package com.atanana.sicounter.screens.main

import com.atanana.sicounter.view.player_control.PlayerControl

interface MainView {
    var selectedPrice: Int

    fun showAddPlayerDialog()

    fun showResetDialog()

    fun showQuitDialog()

    fun appendLogs(line: String)

    fun addPlayerControl(playerControl: PlayerControl)
}