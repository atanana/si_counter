package com.atanana.sicounter.di

import com.atanana.sicounter.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(LogModule::class))
@MainScope
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}