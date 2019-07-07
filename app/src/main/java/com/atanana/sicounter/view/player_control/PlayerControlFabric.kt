package com.atanana.sicounter.view.player_control

import android.content.Context
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.atanana.sicounter.R

class PlayerControlFabric(private val context: Context) {
    private val margin: Int by lazy { (context.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin) * 1.5).toInt() }

    fun build(): PlayerControl {
        val playerControl = PlayerControl(ContextThemeWrapper(context, R.style.AppTheme))
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.marginEnd = margin
        playerControl.layoutParams = layoutParams
        return playerControl
    }
}