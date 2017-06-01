package com.atanana.sicounter.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.WindowManager

fun pxToDp(pixels: Int, resources: Resources): Float {
    return pixels / resources.displayMetrics.scaledDensity
}

fun dpToPx(dp: Int, resources: Resources): Float {
    return dp * resources.displayMetrics.scaledDensity
}

fun screenSize(context: Context): ScreenSize {
    val windowService = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowService.defaultDisplay
    val point = Point()
    display.getSize(point)
    return ScreenSize(point.x, point.y)
}

data class ScreenSize(val width: Int, val height: Int)