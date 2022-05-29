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
import com.atanana.sicounter.screens.main.*
import com.atanana.sicounter.usecases.SaveLogUseCase
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single { HistoryPersistence(androidContext()) }

    scope<HistoryActivity> {
        factory { (activity: HistoryActivity) ->
            HistoryRouter(activity)
        }
        factory { (view: HistoryView, router: HistoryRouter) ->
            HistoryPresenter(get(), view, router)
        }
    }

    scope<MainActivity> {
        scoped { HistoryReportHelper(get(), get()) }
        scoped { (activity: MainActivity) ->
            MainRouter(activity)
        }
        scoped { SaveLogUseCase(get(), get()) }
        scoped { LogNameModel() }
        scoped { ScoresModel(get()) }
        scoped { (view: MainView) ->
            ScoresPresenter(view, get(), get())
        }
        scoped { HistoryModel(get(), get()) }
        scoped { PlayerControlFabric(get()) }
        scoped { ScoreHistoryFormatter(get()) }
        scoped { (router: MainRouter, view: MainView) ->
            MainUiPresenter(
                view, router,
                get(), get(), get(), get()
            )
        }
    }
}