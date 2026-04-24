package com.contraomnese.weather.app

import android.app.Application
import com.contraomnese.weather.di.appModule
import com.contraomnese.weather.di.dataModule
import com.contraomnese.weather.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(
                appModule,
                dataModule,
                domainModule
            )
        }
    }

}