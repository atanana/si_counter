package com.atanana.sicounter.fs

import android.os.Environment
import java.io.File

open class FileProvider {
    open fun parent(file: File): File {
        return file.parentFile
    }

    open fun subfolder(file: File, folder: String): File {
        return File(listOf(file.absolutePath, folder).joinToString(File.separator))
    }

    open fun directories(file: File) = file.listFiles()
            ?.asList()
            ?.filter { it.isDirectory }
            ?.map { it.name }
            ?: emptyList()

    open val externalStorage get() = Environment.getExternalStorageDirectory()
}