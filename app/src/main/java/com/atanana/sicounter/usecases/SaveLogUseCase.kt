package com.atanana.sicounter.usecases

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.atanana.sicounter.R
import com.atanana.sicounter.helpers.HistoryReportHelper

open class SaveLogUseCase(
    private val context: Context,
    private val historyReportHelper: HistoryReportHelper
) {
    fun saveReport(uri: Uri?): Boolean = trySaveReport(uri)

    private fun trySaveReport(uri: Uri?): Boolean {
        uri ?: return false
        val outputStream = context.contentResolver.openOutputStream(uri) ?: return false
        outputStream.use { stream ->
            val report = historyReportHelper.createReport().joinToString("\n")
            val writer = stream.writer()
            writer.write(report)
            writer.flush()
        }

        return true
    }
}