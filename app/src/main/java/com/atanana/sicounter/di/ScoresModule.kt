package com.atanana.sicounter.di

import android.content.Context
import android.view.ViewGroup
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import com.atanana.sicounter.presenter.ScoresPresenter
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.DefaultPlayerControlFabric
import dagger.Module
import dagger.Provides
import rx.Observable
import javax.inject.Named

@Module
class ScoresModule(private val addPlayer: Observable<String>) {
    @Provides
    @MainScope
    fun provideScoresModel(context: Context): ScoresModel {
        return ScoresModel(addPlayer, ScoreHistoryFormatter(context))
    }

    @Provides
    @MainScope
    fun provideScoresPresenter(scoresModel: ScoresModel,
                               @Named("scoresContainer") scoresContainer: ViewGroup,
                               priceSelector: PriceSelector,
                               playerControlFabric: DefaultPlayerControlFabric): ScoresPresenter {
        return ScoresPresenter(scoresModel, scoresContainer, playerControlFabric, priceSelector)
    }
}