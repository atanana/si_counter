package com.atanana.sicounter.presenter

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
        //todo show message
        uri ?: return
        val outputStream = activity.contentResolver.openOutputStream(uri) ?: return
        outputStream.use { stream ->
            val report = historyReportHelper.createReport().joinToString("\n")
            val writer = stream.writer()
            writer.write(report)
            writer.flush()
        }
        Toast.makeText(activity, R.string.file_saved_message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val REQUEST_CODE_SAVE_FILE = 1234
    }
}