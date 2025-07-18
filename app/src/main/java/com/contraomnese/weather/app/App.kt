package com.contraomnese.weather.app

import android.app.Application
import com.contraomnese.weather.di.architecturePresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                architecturePresentationModule
            )
        }
    }

}