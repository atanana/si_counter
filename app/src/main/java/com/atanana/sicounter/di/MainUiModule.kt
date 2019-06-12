package com.atanana.sicounter.di

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.atanana.sicounter.MainActivity
import com.atanana.sicounter.R
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.view.PriceSelector
import com.atanana.sicounter.view.ScoresLog
import com.atanana.sicounter.view.player_control.PlayerControlFabric
import com.atanana.sicounter.view.save.SaveToFileView
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Named

@Module
class MainUiModule(private val activity: MainActivity) {
    private val mainView: View = activity.findViewById(android.R.id.content)
    private val newPlayers: Subject<String> = PublishSubject.create()

    @Provides
    @MainScope
    fun provideActivity(): MainActivity = activity

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
    @Named("addDivider")
    fun provideAddDividerButton(): Button {
        return mainView.findViewById(R.id.add_divider) as Button
    }

    @Provides
    @MainScope
    fun provideFabButton(): FloatingActionButton {
        return mainView.findViewById(R.id.add_player) as FloatingActionButton
    }

    @Provides
    @MainScope
    fun providePlayerControlFabric(context: Context): PlayerControlFabric {
        return PlayerControlFabric(context)
    }

    @Provides
    @MainScope
    @Named("resetDialog")
    fun provideResetDialog(scoresModel: ScoresModel): AlertDialog.Builder {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.reset_title)
                .setCancelable(true)
                .setMessage(R.string.reset_message)
                .setPositiveButton(R.string.yes) { _, _ -> scoresModel.reset() }
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
                .setPositiveButton(R.string.yes) { _, _ -> activity.finish() }
                .setNegativeButton(R.string.no, null)
    }

    @Provides
    @MainScope
    @Named("addPlayerDialog")
    fun provideAddPlayerDialog(): AlertDialog.Builder {
        val playerName = EditText(activity)
        playerName.setSingleLine(true)
        return AlertDialog.Builder(activity)
                .setTitle(R.string.player_name_title)
                .setCancelable(true)
                .setView(playerName)
                .setPositiveButton(R.string.ok) { _, _ ->
                    newPlayers.onNext(playerName.text.toString())
                    playerName.text.clear()
                    clearViewFromParent(playerName)
                }
                .setOnCancelListener {
                    playerName.text.clear()
                    clearViewFromParent(playerName)
                }
    }

    @Provides
    @MainScope
    fun provideSaveToFileView(): SaveToFileView {
        return SaveToFileView(activity, null)
    }

    private fun clearViewFromParent(view: View) {
        (view.parent as? ViewGroup)?.removeView(view)
    }

    @Provides
    @MainScope
    @Named("newPlayers")
    fun provideNewPlayersObservable(): Observable<String> {
        return newPlayers
    }
}