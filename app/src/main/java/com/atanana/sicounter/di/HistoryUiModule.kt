package com.atanana.sicounter.di

import android.widget.TextView
import com.atanana.sicounter.HistoryActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.fs.HistoryPersistence
import com.atanana.sicounter.presenter.HistoryPresenter
import dagger.Module
import dagger.Provides

@Module
class HistoryUiModule(private val activity: HistoryActivity) {
    @Provides
    @MainScope
    fun provideHistoryView(): TextView = activity.findViewById(R.id.history_content)

    @Provides
    @MainScope
    fun provideHistoryPresenter(
        historyPersistence: HistoryPersistence,
        historyView: TextView
    ): HistoryPresenter =
        HistoryPresenter(historyPersistence, historyView)
}