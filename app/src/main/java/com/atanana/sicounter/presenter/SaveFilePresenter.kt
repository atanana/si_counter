package com.atanana.sicounter.presenter

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.atanana.sicounter.R
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.log.LogNameModel

open class SaveFilePresenter(
    private val context: Context,
    private val historyReportHelper: HistoryReportHelper,
    private val logNameModel: LogNameModel
) {
    val filename: String
        get() = logNameModel.fullFilename

    fun saveReport(uri: Uri?) {
        val result = trySaveReport(uri)
        val message = if (result) R.string.file_saved_message else R.string.file_save_error
        showToast(message)
    }

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

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}