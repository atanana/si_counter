package com.atanana.sicounter.view.save

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.atanana.sicounter.R
import io.reactivex.Observable

class FilenameView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val filenameView: TextView by lazy { findViewById(R.id.filename_text) as TextView }

    init {
        LayoutInflater.from(context).inflate(R.layout.filename_save, this, true)
    }

    fun setFilenameProvider(provider: Observable<String>) {
        provider.subscribe { filenameView.text = it }
    }
}