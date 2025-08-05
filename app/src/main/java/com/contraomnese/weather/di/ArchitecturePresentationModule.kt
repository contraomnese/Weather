package com.contraomnese.weather.di

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.UseCaseExecutor
import com.contraomnese.weather.presentation.coroutine.AppCoroutineContextProvider
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import org.koin.dsl.module

val architecturePresentationModule = module {

    single<CoroutineContextProvider> {
        AppCoroutineContextProvider()
    }
    single<UseCaseExecutorProvider> {
        ::UseCaseExecutor
    }
    single<NotificationMonitor> { NotificationMonitor() }

}