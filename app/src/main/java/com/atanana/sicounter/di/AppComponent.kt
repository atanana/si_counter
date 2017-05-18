package com.atanana.sicounter.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {
    fun mainComponent(logModule: LogModule): MainComponent
}