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
import com.atanana.sicounter.screens.history.HistoryView
import com.atanana.sicounter.screens.main.MainActivity
import com.atanana.sicounter.screens.main.MainUiPresenter
import com.atanana.sicounter.screens.main.ScoreHistoryFormatter
import com.atanana.sicounter.screens.main.ScoresPresenter
import com.atanana.sicounter.usecases.SaveLogUseCase
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single { HistoryPersistence(androidContext()) }

    scope(named<HistoryActivity>()) {
        factory { (activity: HistoryActivity) ->
            HistoryRouter(activity)
        }
        factory { (view: HistoryView, router: HistoryRouter) ->
            HistoryPresenter(get(), view, router)
        }
    }

    scope(named<MainActivity>()) {
        scoped { HistoryReportHelper(get(), get()) }
        factory { (activity: MainActivity) ->
            MainRouter(activity)
        }
        scoped { SaveLogUseCase(get(), get()) }
        scoped { LogNameModel() }
        scoped { ScoresModel(get()) }
        scoped { ScoresPresenter(get(), get()) }
        scoped { HistoryModel(get(), get()) }
        scoped { PlayerControlFabric(get()) }
        scoped { ScoreHistoryFormatter(get()) }
        factory { (router: MainRouter, uiScope: CoroutineScope) ->
            MainUiPresenter(
                get(), get(),
                router, get(), get(), uiScope
            )
        }
    }
}