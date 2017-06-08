package com.atanana.sicounter

import android.content.Context
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.CrashManagerListener

fun handleCrashes(context: Context) {
    CrashManager.register(context, object : CrashManagerListener() {
        override fun shouldAutoUploadCrashes(): Boolean {
            return true
        }
    })
}