package com.atanana.sicounter.view.player_control

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.atanana.sicounter.R
import com.atanana.sicounter.utils.UNSPECIFIED
import com.atanana.sicounter.utils.exactly
import com.atanana.sicounter.utils.screenSize

class PlayerControlsLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val margin: Int by lazy { (resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)) }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val screenWidth = screenSize(context as Activity).width
        val width = calculateWidth()
        if (width > screenWidth) {
            gravity = Gravity.START
            super.onMeasure(exactly(width), heightMeasureSpec)
        } else {
            gravity = Gravity.CENTER
            super.onMeasure(exactly(screenWidth - margin * 2), heightMeasureSpec)
        }
    }

    private fun calculateWidth(): Int {
        var width = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(UNSPECIFIED, UNSPECIFIED)
            width += child.measuredWidth

            val layoutParams = child.layoutParams as MarginLayoutParams
            width += layoutParams.marginStart + layoutParams.marginEnd
        }
        return width
    }
}