package com.atanana.sicounter.di

import android.content.Context
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.FileProvider
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.logging.LoggerConfiguration
import com.atanana.sicounter.logging.LogsWriter
import com.atanana.sicounter.model.HistoryModel
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

@Module
class LogModule {
    @Provides
    @MainScope
    fun provideSaveLogModel(context: Context, fileProvider: FileProvider, logNameModel: LogNameModel,
                            historyModel: HistoryModel): SaveLogModel {
        val logFolder = externalAppFolder(context, fileProvider)
        LoggerConfiguration.configureLogbackDirectly(logFolder.absolutePath)
        createLogsWriter(historyModel)

        val logFolderModel = LogFolderModel(logFolder, fileProvider, context)
        return SaveLogModel(logNameModel, logFolderModel)
    }

    @Provides
    @MainScope
    fun provideLogNameModel(scoresModel: ScoresModel): LogNameModel {
        val newPlayerNames = scoresModel.newPlayersObservable.map { it.first.name }
        return LogNameModel(newPlayerNames)
    }

    fun createLogsWriter(historyModel: HistoryModel): LogsWriter {
        return LogsWriter(historyModel.historyChangesObservable)
    }

    fun externalAppFolder(context: Context, fileProvider: FileProvider): File {
        val appName = context.resources.getString(R.string.app_name)
        val path = listOf(fileProvider.externalStorage.absolutePath, appName)
                .joinToString(File.separator)
        return File(path)
    }

    @Provides
    @MainScope
    fun provideHistoryReportHelper(historyModel: HistoryModel, scoresModel: ScoresModel): HistoryReportHelper {
        return HistoryReportHelper(historyModel, scoresModel)
    }

    @Provides
    @MainScope
    fun provideSaveLogPresenter(saveLogModel: SaveLogModel,
                                saveToFileView: SaveToFileView,
                                context: Context,
                                activity: MainActivity,
                                historyReportHelper: HistoryReportHelper,
                                logNameModel: LogNameModel): SaveFilePresenter {
        return SaveFilePresenter(context, saveLogModel, saveToFileView, activity, historyReportHelper, logNameModel)
    }

    @Provides
    @MainScope
    fun provideLogsPresenter(historyModel: HistoryModel, scoresLog: ScoresLog): LogsPresenter {
        return LogsPresenter(historyModel.historyChangesObservable, scoresLog)
    }
}