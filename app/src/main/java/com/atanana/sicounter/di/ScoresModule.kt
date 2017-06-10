package com.atanana.sicounter.di

import android.content.Context
import android.view.ViewGroup
import com.atanana.sicounter.model.HistoryModel
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import com.atanana.sicounter.presenter.ScoresPresenter
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Named

@Module
class ScoresModule {
    @Provides
    @MainScope
    fun provideScoresModel(@Named("newPlayers") newPlayers: Observable<String>, historyModel: HistoryModel): ScoresModel {
        return ScoresModel(newPlayers, historyModel)
    }

    @Provides
    @MainScope
    fun provideScoresPresenter(scoresModel: ScoresModel,
                               @Named("scoresContainer") scoresContainer: ViewGroup,
                               priceSelector: PriceSelector,
                               playerControlFabric: PlayerControlFabric): ScoresPresenter {
        return ScoresPresenter(scoresModel, scoresContainer, playerControlFabric, priceSelector)
    }

    @Provides
    @MainScope
    fun provideHistoryModel(context: Context): HistoryModel {
        return HistoryModel(ScoreHistoryFormatter(context))
    }
}