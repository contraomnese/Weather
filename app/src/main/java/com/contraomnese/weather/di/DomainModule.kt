package com.contraomnese.weather.di

import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetCurrentWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetGeoLocationUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsUseCase> {
        GetLocationsUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<GetGeoLocationUseCase> {
        GetGeoLocationUseCase(
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
    factory<GetForecastWeatherUseCase> {
        GetForecastWeatherUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<GetAppSettingsUseCase> {
        GetAppSettingsUseCase(
            repository = get()
        )
    }
    factory<UpdateAppSettingsUseCase> {
        UpdateAppSettingsUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
}