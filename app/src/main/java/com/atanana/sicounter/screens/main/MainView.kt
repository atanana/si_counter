package com.atanana.sicounter.screens.main

import kotlinx.coroutines.CoroutineScope

interface MainView {
    val uiScope: CoroutineScope

    fun showAddPlayerDialog()

    fun showResetDialog()

    fun showQuitDialog()
}