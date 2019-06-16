package com.atanana.sicounter.di

import android.content.Context
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.presenter.LogsPresenter
import com.atanana.sicounter.presenter.SaveFilePresenter
import com.atanana.sicounter.view.ScoresLog
import dagger.Module
import dagger.Provides

@Module
class LogModule {
    @Provides
    @MainScope
    fun provideLogNameModel(scoresModel: ScoresModel): LogNameModel {
        val newPlayerNames = scoresModel.newPlayersObservable.map { it.first.name }
        return LogNameModel(newPlayerNames)
    }

    @Provides
    @MainScope
    fun provideHistoryReportHelper(
        historyModel: HistoryModel,
        scoresModel: ScoresModel
    ): HistoryReportHelper {
        return HistoryReportHelper(historyModel, scoresModel)
    }

    @Provides
    @MainScope
    fun provideSaveLogPresenter(
        activity: MainActivity,
        historyReportHelper: HistoryReportHelper,
        logNameModel: LogNameModel
    ): SaveFilePresenter = SaveFilePresenter(activity, historyReportHelper, logNameModel)

    @Provides
    @MainScope
    fun provideLogsPresenter(historyModel: HistoryModel, scoresLog: ScoresLog): LogsPresenter {
        return LogsPresenter(historyModel.historyChangesObservable, scoresLog)
    }

    @Provides
    @MainScope
    fun provideHistoryPersistence(context: Context): HistoryPersistence =
        HistoryPersistence(context)
}