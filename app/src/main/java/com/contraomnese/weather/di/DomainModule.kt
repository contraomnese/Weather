package com.contraomnese.weather.di

import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.locationForecast.usecase.GetCurrentWeatherUseCase
import com.contraomnese.weather.domain.locationForecast.usecase.GetLocationForecastUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsUseCase> {
        GetLocationsUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<GetLocationForecastUseCase> {
        GetLocationForecastUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<GetCurrentWeatherUseCase> {
        GetCurrentWeatherUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
}