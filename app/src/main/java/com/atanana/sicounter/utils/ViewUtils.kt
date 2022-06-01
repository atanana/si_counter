package com.atanana.sicounter.utils

import android.app.Activity
import android.content.ContextWrapper
import android.view.View

fun View.getActivity(): Activity? {
    var ctx = context
    while (ctx is ContextWrapper) {
        if (ctx is Activity) {
            return ctx
        } else {
            ctx = ctx.baseContext
        }
    }

    return null
}