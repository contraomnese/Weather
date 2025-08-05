package com.contraomnese.weather.app

import android.app.Application
import com.contraomnese.weather.di.appModule
import com.contraomnese.weather.di.architecturePresentationModule
import com.contraomnese.weather.di.dataModule
import com.contraomnese.weather.di.domainModule
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
                appModule,
                architecturePresentationModule,
                dataModule,
                domainModule
            )
        }
    }

}