package com.atanana.sicounter.di

import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.presenter.HistoryPresenter
import dagger.Module
import dagger.Provides

@Module
class HistoryUiModule(private val activity: HistoryActivity) {
    @Provides
    @MainScope
    fun provideHistoryPresenter(
        historyPersistence: HistoryPersistence
    ): HistoryPresenter =
        HistoryPresenter(historyPersistence, activity)
}