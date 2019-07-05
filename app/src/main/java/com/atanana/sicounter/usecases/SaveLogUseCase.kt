package com.atanana.sicounter.usecases

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.atanana.sicounter.R
import com.atanana.sicounter.helpers.HistoryReportHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream

open class SaveLogUseCase(
    private val context: Context,
    private val historyReportHelper: HistoryReportHelper
) {
    suspend fun saveReport(uri: Uri?) {
        val result = trySaveReport(uri)
        val message = if (result) R.string.file_saved_message else R.string.file_save_error
        showToast(message)
    }

    private suspend fun trySaveReport(uri: Uri?): Boolean {
        return try {
            uri ?: return false
            val outputStream = openStream(uri) ?: return false
            outputStream.use { stream ->
                val report = createReport()
                writeReport(stream, report)
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun writeReport(stream: OutputStream, report: String) {
        withContext(Dispatchers.IO) {
            val writer = stream.writer()
            writer.write(report)
            writer.flush()
        }
    }

    private suspend fun createReport(): String = withContext(Dispatchers.Default) {
        historyReportHelper.createReport().joinToString("\n")
    }

    private suspend fun openStream(uri: Uri) =
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(uri)
        }

    private suspend fun showToast(@StringRes message: Int) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}