package com.atanana.sicounter.view.player_control

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.atanana.sicounter.MissingId
import com.atanana.sicounter.data.PartialScoreAction
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreActionType
import com.atanana.sicounter.data.ScoreActionType.MINUS
import com.atanana.sicounter.data.ScoreActionType.PLUS
import com.atanana.sicounter.safeThrow
import com.atanana.sicounter.utils.UNSPECIFIED
import com.atanana.sicounter.utils.dpToPx
import com.atanana.sicounter.utils.getActivity
import com.atanana.sicounter.utils.pxToDp
import com.atanana.sicounter.utils.screenSize
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.max
import kotlin.math.min

class PlayerControl(context: Context) : LinearLayout(context, null, 0) {
    private val playerName: TextView = TextView(context)
    private val playerScore: TextView = TextView(context)
    private val addScore: Button = Button(context).apply {
        setOnClickListener {
            trySendScoreAction(PLUS)
        }
    }

    private val subtractScore: Button = Button(context).apply {
        setOnClickListener {
            trySendScoreAction(MINUS)
        }
    }

    private var playerId: Int? = null

    private val scoreFlow = MutableSharedFlow<PartialScoreAction>(extraBufferCapacity = 1)

    val scoreActions = scoreFlow.asSharedFlow()

    init {
        orientation = VERTICAL

        style(playerName)
        addView(playerName)

        addScore.text = "+"
        addView(addScore)

        style(playerScore)
        addView(playerScore)

        subtractScore.text = "-"
        addView(subtractScore)
    }

    private fun style(view: TextView) {
        view.textAlignment = View.TEXT_ALIGNMENT_CENTER
        view.typeface = Typeface.DEFAULT_BOLD
        view.setSingleLine()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val height = bottom - top
        val slotHeight = height / 3
        val slotPadding = (width - slotHeight) / 2
        val labelHeight = slotHeight / 2

        val labelTextSize = pxToDp(labelHeight, resources) * 2 / 3
        val buttonTextSize = pxToDp(slotHeight, resources) / 2

        playerName.textSize = labelTextSize
        playerName.layout(0, top, width, top + labelHeight)

        addScore.layout(
            slotPadding,
            top + labelHeight,
            width - slotPadding,
            top + labelHeight + slotHeight
        )
        addScore.textSize = buttonTextSize

        playerScore.layout(
            0,
            top + labelHeight + slotHeight,
            width,
            top + labelHeight * 2 + slotHeight
        )
        playerScore.textSize = labelTextSize

        subtractScore.layout(
            slotPadding,
            top + labelHeight * 2 + slotHeight,
            width - slotPadding,
            bottom
        )
        subtractScore.textSize = buttonTextSize
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val screenHeight = screenSize(getActivity()!!).height
        val height = min((screenHeight / 2.5).toFloat(), dpToPx(300, resources))
        playerName.measure(UNSPECIFIED, UNSPECIFIED)
        val slotWidth = (height / 3).toInt()
        val width = max(slotWidth, playerName.measuredWidth)

        addScore.layoutParams = LayoutParams(slotWidth, slotWidth)
        subtractScore.layoutParams = LayoutParams(slotWidth, slotWidth)

        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthSpec, heightSpec)
    }

    private fun trySendScoreAction(type: ScoreActionType) {
        val playerId = playerId
        if (playerId != null) {
            scoreFlow.tryEmit(PartialScoreAction(type, playerId))
        } else {
            safeThrow { MissingId() }
        }
    }

    fun update(score: Score, id: Int? = null) {
        playerName.text = score.name
        playerScore.text = score.score.toString()
        if (id != null) {
            playerId = id
        }
    }
}
