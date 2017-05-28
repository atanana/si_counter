package com.atanana.sicounter.di

import android.content.Context
import android.support.v7.app.AlertDialog
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.FileProvider
import com.atanana.sicounter.logging.LoggerConfiguration
import com.atanana.sicounter.logging.LogsWriter
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogFolderModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.model.log.SaveLogModel
import com.atanana.sicounter.presenter.LogsPresenter
import com.atanana.sicounter.presenter.SaveFilePresenter
import com.atanana.sicounter.view.ScoresLog
import com.atanana.sicounter.view.save.SaveToFileView
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Named

@Module
class LogModule {
    @Provides
    @MainScope
    fun provideSaveLogModel(context: Context, fileProvider: FileProvider, scoresModel: ScoresModel): SaveLogModel {
        val logFolder = externalAppFolder(context, fileProvider)
        LoggerConfiguration.configureLogbackDirectly(logFolder.absolutePath)
        createLogsWriter(scoresModel)

        val logNameModel = createLogNameModel(scoresModel)
        val logFolderModel = LogFolderModel(logFolder, fileProvider, context)
        return SaveLogModel(logNameModel, logFolderModel)
    }

    fun createLogNameModel(scoresModel: ScoresModel): LogNameModel {
        val newPlayerNames = scoresModel.newPlayersObservable.map { it.first.name }
        return LogNameModel(newPlayerNames)
    }

    fun createLogsWriter(scoresModel: ScoresModel): LogsWriter {
        return LogsWriter(scoresModel.historyChangesObservable)
    }

    fun externalAppFolder(context: Context, fileProvider: FileProvider): File {
        val appName = context.resources.getString(R.string.app_name)
        val path = listOf(fileProvider.externalStorage.absolutePath, appName)
                .joinToString(File.separator)
        return File(path)
    }

    @Provides
    @MainScope
    fun provideSaveLogPresenter(saveLogModel: SaveLogModel,
                                saveToFileView: SaveToFileView,
                                context: Context,
                                @Named("saveResultsDialog") dialogBuilder: AlertDialog.Builder): SaveFilePresenter {
        return SaveFilePresenter(context, saveLogModel, saveToFileView, dialogBuilder)
    }

    @Provides
    @MainScope
    fun provideLogsPresenter(scoresModel: ScoresModel, scoresLog: ScoresLog): LogsPresenter {
        return LogsPresenter(scoresModel.historyChangesObservable, scoresLog)
    }
}