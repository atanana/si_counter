package com.atanana.sicounter.di

import android.content.Context
import com.atanana.sicounter.fs.FileProvider
import com.atanana.sicounter.model.log.LogFolderModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.model.log.SaveLogModel
import dagger.Module
import dagger.Provides
import rx.Observable
import java.io.File

@Module
class LogModule(private val logFolder: File,
                private val newPlayers: Observable<String>) {
    @Provides
    @MainScope
    fun provideSaveLogModel(context: Context): SaveLogModel {
        val logNameModel = LogNameModel(newPlayers)
        val logFolderModel = LogFolderModel(logFolder, FileProvider(), context)
        return SaveLogModel(logNameModel, logFolderModel)
    }
}