package com.contraomnese.weather.di

import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsUseCase> { GetLocationsUseCase(repository = get(), coroutineContextProvider = get()) }
}