package com.atanana.sicounter.presenter

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.log.LogNameModel

open class SaveFilePresenter(
    private val activity: MainActivity,
    private val historyReportHelper: HistoryReportHelper,
    private val logNameModel: LogNameModel
) {

    open fun showDialog() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            .apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, logNameModel.fullFilename)
                addCategory(Intent.CATEGORY_OPENABLE)
            }
        activity.startActivityForResult(intent, REQUEST_CODE_SAVE_FILE)
    }

    fun saveReport(uri: Uri?) {
        val result = trySaveReport(uri)
        val message = if (result) R.string.file_saved_message else R.string.file_save_error
        showToast(message)
    }

    private fun trySaveReport(uri: Uri?): Boolean {
        uri ?: return false
        val outputStream = activity.contentResolver.openOutputStream(uri) ?: return false
        outputStream.use { stream ->
            val report = historyReportHelper.createReport().joinToString("\n")
            val writer = stream.writer()
            writer.write(report)
            writer.flush()
        }

        return true
    }

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val REQUEST_CODE_SAVE_FILE = 1234
    }
}