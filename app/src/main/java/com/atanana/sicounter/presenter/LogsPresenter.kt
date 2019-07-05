package com.atanana.sicounter.presenter

import com.atanana.sicounter.view.ScoresLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext

class LogsPresenter(
    private val historyChanges: ReceiveChannel<String>
) {
    suspend fun connect(logsView: ScoresLog) {
        for (change in historyChanges) {
            withContext(Dispatchers.Main) {
                logsView.append(change + "\n")
            }
        }
    }
}