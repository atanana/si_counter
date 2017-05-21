package com.atanana.sicounter.di

import com.atanana.sicounter.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(LogModule::class, ScoresModule::class, MainUiModule::class))
@MainScope
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}