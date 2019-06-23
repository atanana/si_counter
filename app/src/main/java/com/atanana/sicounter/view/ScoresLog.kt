package com.atanana.sicounter.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import android.widget.TextView
import com.atanana.sicounter.R

class ScoresLog(context: Context, attrs: AttributeSet?) : ScrollView(context, attrs) {
    private val logText: TextView by lazy { findViewById<TextView>(R.id.log_text) }

    init {
        inflate(context, R.layout.scores_log, this)
    }

    fun append(text: String) {
        logText.append(text)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        fullScroll(FOCUS_DOWN)
    }
}