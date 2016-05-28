package com.atanana.sicounter.presenter

import android.content.Context
import android.text.Html
import android.widget.TextView
import com.atanana.sicounter.R
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.model.ScoresModel
import com.atanana.sicounter.presenter.ScoreActionFormatter.format
import rx.Observable

class LogsPresenter(private val scoresModel: ScoresModel,
                    private val scoreActions: Observable<ScoreAction>,
                    private val logsView: TextView) {
    private val context: Context = logsView.context
    private val newPlayerTemplate: String = context.resources.getText(R.string.new_player_log).toString()

    init {
        scoresModel.newPlayers.subscribe({ newPlayer ->
            logsView.append(Html.fromHtml(String.format(newPlayerTemplate, newPlayer.first.name) + "<br/>"))
        })
        scoreActions.subscribe({ scoreAction ->
            logsView.append(format(scoreAction, scoresModel) + "\n")
        })
    }
}