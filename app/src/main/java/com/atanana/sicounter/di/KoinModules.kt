package com.atanana.sicounter.di

import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.screens.history.HistoryActivity
import com.atanana.sicounter.screens.history.HistoryPresenter
import com.atanana.sicounter.screens.history.HistoryRouter
import com.atanana.sicounter.screens.main.MainActivity
import com.atanana.sicounter.screens.main.MainUiPresenter
import com.atanana.sicounter.screens.main.ScoreHistoryFormatter
import com.atanana.sicounter.screens.main.ScoresPresenter
import com.atanana.sicounter.usecases.SaveLogUseCase
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single { HistoryPersistence(androidContext()) }

    scope<HistoryActivity> {
        factory { HistoryRouter(get()) }
        factory { HistoryPresenter(get(), get(), get()) }
    }

    scope<MainActivity> {
        scoped { HistoryReportHelper(get(), get()) }
        scoped { MainRouter(get()) }
        scoped { SaveLogUseCase(get(), get()) }
        scoped { LogNameModel() }
        scoped { ScoresModel(get()) }
        scoped { ScoresPresenter(get(), get(), get()) }
        scoped { HistoryModel(get(), get()) }
        scoped { PlayerControlFabric(get()) }
        scoped { ScoreHistoryFormatter(get()) }
        scoped { MainUiPresenter(get(), get(), get(), get(), get(), get()) }
    }
}