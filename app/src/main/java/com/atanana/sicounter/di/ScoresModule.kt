package com.atanana.sicounter.di

import com.atanana.sicounter.model.ScoresModel
import dagger.Module
import dagger.Provides

@Module
class ScoresModule(private val scoresModel: ScoresModel) {
    @Provides
    @MainScope
    fun provideScoresModel(): ScoresModel {
        return scoresModel
    }
}