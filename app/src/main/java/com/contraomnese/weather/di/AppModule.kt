package com.contraomnese.weather.di

import com.contraomnese.weather.MainActivityViewModel
import com.contraomnese.weather.workers.WeatherUpdateWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainActivityViewModel)
    workerOf(::WeatherUpdateWorker)
}