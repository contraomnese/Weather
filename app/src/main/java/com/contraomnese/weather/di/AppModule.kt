package com.contraomnese.weather.di

import androidx.work.WorkManager
import com.contraomnese.weather.MainActivityViewModel
import com.contraomnese.weather.workers.FavoritesForecastUpdateWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val APPLICATION_COROUTINE_SCOPE = "ApplicationScope"
private const val I0_DISPATCHER = "IoDispatcher"

val appModule = module {
    viewModelOf(::MainActivityViewModel)
    workerOf(::FavoritesForecastUpdateWorker)
    single(named(APPLICATION_COROUTINE_SCOPE)) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    single(named(I0_DISPATCHER)) { Dispatchers.IO }
    single { WorkManager.getInstance(androidContext()) }
}