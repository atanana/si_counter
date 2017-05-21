package com.atanana.sicounter.di

import android.view.View
import com.atanana.sicounter.R
import com.atanana.sicounter.view.ScoresLog
import dagger.Module
import dagger.Provides

@Module
class MainUiModule(private val mainView: View) {
    @Provides
    @MainScope
    fun provideLogView(): ScoresLog {
        return mainView.findViewById(R.id.log_view) as ScoresLog
    }
}