package com.atanana.sicounter.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
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
        var width = activity.resources.displayMetrics.widthPixels
        var height = activity.resources.displayMetrics.heightPixels
        val insets = getInsetsCompat(activity.window.decorView)
        if (insets != null) {
            width = width - insets.left - insets.right
            height = height - insets.top - insets.bottom
        }
        ScreenSize(width, height)
    }
}

private fun getInsetsCompat(decorView: View): Insets? {
    val rootWindowInsets = ViewCompat.getRootWindowInsets(decorView) ?: return null
    return rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
}