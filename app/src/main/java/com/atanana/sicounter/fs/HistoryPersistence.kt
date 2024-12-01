package com.atanana.sicounter.fs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val FILENAME = "history.txt"

class HistoryPersistence @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun addHistory(item: String) = withContext(Dispatchers.IO) {
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE or Context.MODE_APPEND).use {
            val writer = it.bufferedWriter()
            writer.write(item)
            writer.newLine()
            writer.flush()
        }
    }

    suspend fun getAllHistory(): List<String> = withContext(Dispatchers.IO) {
        try {
            context.openFileInput(FILENAME).use {
                it.bufferedReader().readLines()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        context.deleteFile(FILENAME)
    }
}
