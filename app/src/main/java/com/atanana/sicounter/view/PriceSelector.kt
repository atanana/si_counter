package com.atanana.sicounter.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.atanana.sicounter.R

class PriceSelector(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.price_selector, this)
    }
}