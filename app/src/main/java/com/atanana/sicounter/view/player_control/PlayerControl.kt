package com.atanana.sicounter.view.player_control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

open class PlayerControl(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val playerName: TextView by lazy { findViewById(R.id.player_name) as TextView }
    private val playerScore: TextView by lazy { findViewById(R.id.player_score) as TextView }
    private val addScore: View by lazy { findViewById(R.id.add_score) }
    private val subtractScore: View by lazy { findViewById(R.id.subtract_score) }
    private val _scoreActions: Subject<ScoreAction, ScoreAction> = PublishSubject()

    open val scoreActions: Observable<ScoreAction>
        get() = _scoreActions

    init {
        LayoutInflater.from(context).inflate(R.layout.player_control, this)
    }

    open fun update(score: Score, id: Int? = null) {
        playerName.text = score.name
        playerScore.text = score.score.toString()

        if (id != null) {
            addScore.setOnClickListener { _scoreActions.onNext(ScoreAction(PLUS, id)) }
            subtractScore.setOnClickListener { _scoreActions.onNext(ScoreAction(MINUS, id)) }
        }
    }
}