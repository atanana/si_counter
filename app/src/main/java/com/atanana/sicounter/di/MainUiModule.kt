package com.atanana.sicounter.di

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.atanana.sicounter.R
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.ScoresLog
import com.atanana.sicounter.view.player_control.DefaultPlayerControlFabric
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MainUiModule(private val mainView: View) {
    @Provides
    @MainScope
    fun provideLogView(): ScoresLog {
        return mainView.findViewById(R.id.log_view) as ScoresLog
    }

    @Provides
    @MainScope
    fun providePriceSelector(): PriceSelector {
        return mainView.findViewById(R.id.price_selector) as PriceSelector
    }

    @Provides
    @MainScope
    @Named("scoresContainer")
    fun provideScoresContainer(): ViewGroup {
        return mainView.findViewById(R.id.scores_container) as ViewGroup
    }

    @Provides
    @MainScope
    fun provideDefaultPlayerControlFabric(context: Context): DefaultPlayerControlFabric {
        return DefaultPlayerControlFabric(context)
    }
}