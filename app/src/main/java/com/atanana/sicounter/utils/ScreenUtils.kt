package com.atanana.sicounter.utils

import android.app.Activity
import android.content.res.Resources
import androidx.window.layout.WindowMetricsCalculator

fun pxToDp(pixels: Int, resources: Resources): Float {
    return pixels / resources.displayMetrics.scaledDensity
}

fun dpToPx(dp: Int, resources: Resources): Float {
    return dp * resources.displayMetrics.scaledDensity
}

fun screenSize(activity: Activity): ScreenSize {
    val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    val bounds = windowMetrics.bounds
    return ScreenSize(bounds.width(), bounds.height())
}

data class ScreenSize(val width: Int, val height: Int)