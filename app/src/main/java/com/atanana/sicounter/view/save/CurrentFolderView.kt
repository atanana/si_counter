package com.atanana.sicounter.view.save

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.atanana.sicounter.R
import io.reactivex.Observable

class CurrentFolderView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val folderView: TextView by lazy { findViewById(R.id.folder_text) as TextView }

    init {
        LayoutInflater.from(context).inflate(R.layout.current_folder, this, true)
    }

    fun setCurrentFolderProvider(observable: Observable<String>) {
        observable.subscribe { folder -> folderView.text = folder }
    }
}