package com.atanana.sicounter.fs

import android.content.Context

class HistoryPersistence(private val context: Context) {
    fun addHistory(item: String) {
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE or Context.MODE_APPEND).use {
            val writer = it.bufferedWriter()
            writer.write(item)
            writer.newLine()
            writer.flush()
        }
    }

    fun getAllHistory(): List<String> =
        try {
            context.openFileInput(FILENAME).use {
                it.bufferedReader().readLines()
            }
        } catch (e: Exception) {
            emptyList()
        }

    fun clearHistory() {
        context.deleteFile(FILENAME)
    }

    companion object {
        const val FILENAME = "history.txt"
    }
}