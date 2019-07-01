package com.atanana.sicounter

import android.app.Application
import com.atanana.sicounter.di.mainModule
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule)
        }

        if (!BuildConfig.DEBUG) {
            RxJavaPlugins.setErrorHandler { }
        }
    }
}