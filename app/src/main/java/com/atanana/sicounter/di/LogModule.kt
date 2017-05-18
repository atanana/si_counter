package com.atanana.sicounter.di

import android.content.Context
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.FileProvider
import com.atanana.sicounter.logging.LoggerConfiguration
import com.atanana.sicounter.model.log.LogFolderModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.model.log.SaveLogModel
import dagger.Module
import dagger.Provides
import rx.Observable
import java.io.File

@Module
class LogModule(private val newPlayers: Observable<String>) {
    @Provides
    @MainScope
    fun provideSaveLogModel(context: Context, fileProvider: FileProvider): SaveLogModel {
        val logFolder = externalAppFolder(context, fileProvider)
        LoggerConfiguration.configureLogbackDirectly(logFolder.absolutePath)

        val logNameModel = LogNameModel(newPlayers)
        val logFolderModel = LogFolderModel(logFolder, fileProvider, context)
        return SaveLogModel(logNameModel, logFolderModel)
    }

    fun externalAppFolder(context: Context, fileProvider: FileProvider): File {
        val appName = context.resources.getString(R.string.app_name)
        val path = listOf(fileProvider.externalStorage.absolutePath, appName)
                .joinToString(File.separator)
        return File(path)
    }
}