package com.contraomnese.weather.di

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.presentation.coroutine.AppCoroutineContextProvider
import org.koin.dsl.module

val architecturePresentationModule = module {

    single<CoroutineContextProvider> {
        AppCoroutineContextProvider()
    }

}