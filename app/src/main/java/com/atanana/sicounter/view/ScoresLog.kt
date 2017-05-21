package com.atanana.sicounter.view

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ScrollView
import android.widget.TextView
import com.atanana.sicounter.R

open class ScoresLog(context: Context, attrs: AttributeSet?) : ScrollView(context, attrs) {
    private val logText: TextView by lazy { findViewById(R.id.log_text) as TextView }

    init {
        LayoutInflater.from(context).inflate(R.layout.scores_log, this)
    }

    open fun append(text: String) {
        logText.append(text)
        scrollToBottom()
    }

    fun append(text: Spanned) {
        logText.append(text)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        fullScroll(FOCUS_DOWN)
    }
}