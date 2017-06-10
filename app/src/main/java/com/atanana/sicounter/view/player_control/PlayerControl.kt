package com.atanana.sicounter.view.player_control

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import com.atanana.sicounter.utils.dpToPx
import com.atanana.sicounter.utils.pxToDp
import com.atanana.sicounter.utils.screenSize
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

open class PlayerControl(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var playerName: TextView = TextView(context)
    private var playerScore: TextView = TextView(context)
    private val addScore: Button = Button(context)
    private val subtractScore: Button = Button(context)
    private val _scoreActions = PublishSubject.create<ScoreAction>()

    open val scoreActions: Observable<ScoreAction>
        get() = _scoreActions

    init {
        orientation = LinearLayout.VERTICAL

        playerName.textAlignment = View.TEXT_ALIGNMENT_CENTER
        playerName.typeface = Typeface.DEFAULT_BOLD
        addView(playerName)

        addScore.text = "+"
        addView(addScore)

        playerScore.textAlignment = View.TEXT_ALIGNMENT_CENTER
        playerScore.typeface = Typeface.DEFAULT_BOLD
        addView(playerScore)

        subtractScore.text = "-"
        addView(subtractScore)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val height = bottom - top
        val slotHeight = height / 3
        val labelHeight = slotHeight / 2

        val labelTextSize = pxToDp(labelHeight, resources) * 2 / 3
        val buttonTextSize = pxToDp(slotHeight, resources) / 2

        playerName.textSize = labelTextSize
        playerName.layout(0, top, width, top + labelHeight)

        addScore.layout(0, top + labelHeight, width, top + labelHeight + slotHeight)
        addScore.textSize = buttonTextSize

        playerScore.layout(0, top + labelHeight + slotHeight, width, top + labelHeight * 2 + slotHeight)
        playerScore.textSize = labelTextSize

        subtractScore.layout(0, top + labelHeight * 2 + slotHeight, width, bottom)
        subtractScore.textSize = buttonTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val screenHeight = screenSize(context).height
        val height = Math.min((screenHeight / 2.5).toFloat(), dpToPx(300, resources))
        val widthSpec = MeasureSpec.makeMeasureSpec((height / 3).toInt(), MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthSpec, heightSpec)
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