package com.atanana.sicounter.fs

import android.content.Context
import android.os.Environment
import com.atanana.sicounter.R
import java.io.File

object FileSystemConfiguration {
    fun externalAppFolder(context: Context): String {
        val appName = context.resources.getString(R.string.app_name)
        return listOf(Environment.getExternalStorageDirectory().absolutePath, appName)
                .joinToString(File.separator)
    }
}