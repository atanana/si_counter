package com.atanana.sicounter.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Score

class PlayerControl(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val playerName: TextView by lazy { findViewById(R.id.player_name) as TextView }
    private val playerScore: TextView by lazy { findViewById(R.id.player_score) as TextView }
    private val addScore: View by lazy { findViewById(R.id.add_score) }
    private val subtractScore: View by lazy { findViewById(R.id.subtract_score) }

    init {
        LayoutInflater.from(context).inflate(R.layout.player_control, this)
    }

    fun update(score: Score) {
        playerName.text = score.name
        playerScore.text = score.score.toString()
    }
}