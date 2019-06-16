package com.atanana.sicounter.di

import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.presenter.MainUiPresenter
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun mainComponent(
        logModule: LogModule,
        scoresModule: ScoresModule,
        mainUiModule: MainUiModule
    ): MainComponent

    fun historyComponent(historyUiModule: HistoryUiModule): HistoryComponent
}

@Subcomponent(modules = [LogModule::class, ScoresModule::class, MainUiModule::class])
@MainScope
interface MainComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(mainUiPresenter: MainUiPresenter)
}

@Subcomponent(modules = [HistoryUiModule::class])
@MainScope
interface HistoryComponent {
    fun inject(historyActivity: HistoryActivity)
}