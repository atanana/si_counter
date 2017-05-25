package com.atanana.sicounter.di

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.ScoresLog
import com.atanana.sicounter.view.player_control.DefaultPlayerControlFabric
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MainUiModule(private val activity: MainActivity) {
    private val mainView: View = activity.findViewById(android.R.id.content)

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

    @Provides
    @MainScope
    @Named("resetDialog")
    fun provideResetDialog(scoresModel: ScoresModel): AlertDialog.Builder {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.reset_title)
                .setCancelable(true)
                .setMessage(R.string.reset_message)
                .setPositiveButton(R.string.yes, { _, _ -> scoresModel.reset() })
                .setNegativeButton(R.string.no, null)
    }

    @Provides
    @MainScope
    @Named("exitDialog")
    fun provideExitDialog(): AlertDialog.Builder {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.close_title)
                .setCancelable(true)
                .setMessage(R.string.close_message)
                .setPositiveButton(R.string.yes, { _, _ -> activity.finish() })
                .setNegativeButton(R.string.no, null)
    }
}