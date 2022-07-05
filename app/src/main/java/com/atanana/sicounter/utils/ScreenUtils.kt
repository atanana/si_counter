package com.atanana.sicounter.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.view.WindowInsets
import androidx.core.view.WindowInsetsCompat

fun pxToDp(pixels: Int, resources: Resources): Float {
    return pixels / resources.displayMetrics.scaledDensity
}

fun dpToPx(dp: Int, resources: Resources): Float {
    return dp * resources.displayMetrics.scaledDensity
}

data class ScreenSize(val width: Int, val height: Int)

fun screenSize(activity: Activity): ScreenSize {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = activity.windowManager.currentWindowMetrics
        val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        val width = metrics.bounds.width() - insets.left - insets.right
        val height = metrics.bounds.height() - insets.top - insets.bottom
        ScreenSize(width, height)
    } else {
        val view = activity.window.decorView
        val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view).getInsets(WindowInsetsCompat.Type.systemBars())
        val width = activity.resources.displayMetrics.widthPixels - insets.left - insets.right
        val height = activity.resources.displayMetrics.heightPixels - insets.top - insets.bottom
        ScreenSize(width, height)
    }
}