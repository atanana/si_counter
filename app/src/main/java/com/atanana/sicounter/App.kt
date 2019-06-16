package com.atanana.sicounter

import android.app.Application
import com.atanana.sicounter.di.AppComponent
import com.atanana.sicounter.di.AppModule
import com.atanana.sicounter.di.DaggerAppComponent
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {
    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        if (!BuildConfig.DEBUG) {
            RxJavaPlugins.setErrorHandler { }
        }
    }
}