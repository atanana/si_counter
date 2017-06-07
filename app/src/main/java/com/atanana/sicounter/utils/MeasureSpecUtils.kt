package com.atanana.sicounter.utils

import android.view.View

val UNSPECIFIED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

fun exactly(size: Int) = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)