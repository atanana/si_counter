package com.atanana.sicounter.di

import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.helpers.HistoryReportHelper
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.model.log.LogNameModel
import com.atanana.sicounter.presenter.*
import com.atanana.sicounter.router.MainRouter
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single { HistoryPersistence(androidContext()) }

    scope(named<HistoryActivity>()) {
        scoped { HistoryPresenter(get(), get()) }
    }

    scope(named<MainActivity>()) {
        scoped { HistoryReportHelper(get(), get()) }
        factory { (activity: MainActivity) ->
            MainRouter(activity)
        }
        scoped { SaveFilePresenter(get(), get(), get()) }
        scoped {
            val historyChanges = get<HistoryModel>().historyChangesObservable
            LogsPresenter(historyChanges, get())
        }
        scoped {
            val newPlayerNames = get<ScoresModel>().newPlayersObservable.map { it.first.name }
            LogNameModel(newPlayerNames)
        }
        scoped { ScoresModel(get(named("newPlayers")), get()) }
        scoped { ScoresPresenter(get(), get(named("scoresContainer")), get(), get()) }
        scoped { HistoryModel(get(), get()) }
        scoped { PlayerControlFabric(androidContext()) }
        scoped(named("newPlayers")) { PublishSubject.create<String>() }

//        scoped {
//            val activity = get<MainActivity>()
//            activity.findViewById<View>(android.R.id.content)
//        }
//        scoped {
//            val mainView = get<View>()
//            mainView.findViewById<ScoresLog>(R.id.log_view)
//        }
//        scoped {
//            val mainView = get<View>()
//            mainView.findViewById<PriceSelector>(R.id.price_selector)
//        }
//        scoped(named("scoresContainer")) {
//            val mainView = get<View>()
//            mainView.findViewById<ViewGroup>(R.id.scores_container)
//        }
        factory { (router: MainRouter) ->
            MainUiPresenter(
                get(named("newPlayers")),
                get(), get(), get(),
                router, get()
            )
        }
    }
}