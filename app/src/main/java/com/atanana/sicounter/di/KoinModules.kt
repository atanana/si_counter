package com.atanana.sicounter.di

import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.presenter.*
import com.atanana.sicounter.router.HistoryRouter
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single { HistoryPersistence(androidContext()) }

    scope(named<HistoryActivity>()) {
        factory { (activity: HistoryActivity) ->
            HistoryRouter(activity)
        }
        factory { (router: HistoryRouter) ->
            HistoryPresenter(get(), router)
        }
    }

    scope(named<MainActivity>()) {
        scoped { HistoryReportHelper(get(), get()) }
        factory { (activity: MainActivity) ->
            MainRouter(activity)
        }
        scoped { SaveFilePresenter(get(), get(), get()) }
        scoped {
            val historyChanges = get<HistoryModel>().historyChangesObservable
            LogsPresenter(historyChanges)
        }
        scoped {
            val newPlayerNames = get<ScoresModel>().newPlayersObservable.map { it.first.name }
            LogNameModel(newPlayerNames)
        }
        scoped { ScoresModel(get(named("newPlayers")), get()) }
        scoped { ScoresPresenter(get(), get()) }
        scoped { HistoryModel(get(), get()) }
        scoped { PlayerControlFabric(get()) }
        scoped { ScoreHistoryFormatter(get()) }
        scoped(named("newPlayers")) { PublishSubject.create<String>() }

        factory { (router: MainRouter) ->
            MainUiPresenter(
                get(named("newPlayers")),
                get(), get(), get(),
                router
            )
        }
    }
}