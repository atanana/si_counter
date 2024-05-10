package com.atanana.sicounter.di

import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.screens.history.HistoryViewModel
import com.atanana.sicounter.screens.main.MainActivity
import com.atanana.sicounter.screens.main.MainUiPresenter
import com.atanana.sicounter.screens.main.MainViewModel
import com.atanana.sicounter.screens.main.ScoreHistoryFormatter
import com.atanana.sicounter.screens.main.ScoresViewModel
import com.atanana.sicounter.usecases.SaveLogUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    single { HistoryPersistence(androidContext()) }

    viewModelOf(::HistoryViewModel)

    scope<MainActivity> {
        scoped { HistoryReportHelper(get(), get()) }
        scoped { SaveLogUseCase(get(), get()) }
        scoped { LogNameModel() }
        scoped { ScoresModel(get()) }
        scoped { HistoryModel(get(), get()) }
        scoped { ScoreHistoryFormatter(get()) }
        scoped { MainUiPresenter(get(), get()) }

        viewModelOf(::MainViewModel)
        viewModelOf(::ScoresViewModel)
    }
}
