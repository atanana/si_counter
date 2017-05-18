package com.atanana.sicounter.di

import android.content.Context
import com.atanana.sicounter.fs.FileProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideFileProvider(): FileProvider {
        return FileProvider()
    }
}