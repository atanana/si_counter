package com.atanana.sicounter.fs

import java.io.File

open class FileProvider {
    fun parent(file: File): File {
        return file.parentFile
    }

    fun subfolder(file: File, folder: String) :File {
        return File(listOf(file.absolutePath, folder).joinToString(File.separator))
    }

    fun directories(file: File) = file.listFiles()
            ?.asList()
            ?.filter { it.isDirectory }
            ?.map { it.name }
            ?: emptyList()
}