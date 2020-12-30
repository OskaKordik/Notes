package com.natife.streaming

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKoin()

    }

    private fun initKoin() {
        startKoin {
            modules(appModules)
            androidContext(this@App)
        }
    }

    fun restartKoin() {
        stopKoin()
        initKoin()
    }
}