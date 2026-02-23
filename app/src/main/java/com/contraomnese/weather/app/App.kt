package com.contraomnese.weather.app

import android.app.Application
import com.contraomnese.weather.di.appModule
import com.contraomnese.weather.di.dataModule
import com.contraomnese.weather.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App: Application() {

//    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase by inject()
//    private var weatherSyncEnabled: Boolean = false

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

//    private fun enableWeatherSync() = CoroutineScope(Dispatchers.IO).launch {
//        observeAppSettingsUseCase().collect {
//            if (it.forecastAutoUpdate) {
//                weatherSyncEnabled = true
//                setupWeatherSync()
//            }
//        }
//    }
//
//    private fun setupWeatherSync() {
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresBatteryNotLow(true)
//            .build()
//
//        val weatherRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(
//            6, TimeUnit.HOURS
//        )
//            .setConstraints(constraints)
//            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
//            .build()
//
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            "WeatherUpdateWork",
//            ExistingPeriodicWorkPolicy.KEEP,
//            weatherRequest
//        )
//    }

}