package com.contraomnese.weather.di

import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetDetailsLocationUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsUseCase> {
        GetLocationsUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<GetDetailsLocationUseCase> {
        GetDetailsLocationUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<AddFavoriteUseCase> {
        AddFavoriteUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<RemoveFavoriteUseCase> {
        RemoveFavoriteUseCase(
            repository = get(),
            coroutineContextProvider = get()
        )
    }
    factory<ObserveFavoritesUseCase> {
        ObserveFavoritesUseCase(
            repository = get()
        )
    }
    factory<ObserveForecastWeatherUseCase> {
        ObserveForecastWeatherUseCase(
            repository = get()
        )
    }
    factory<UpdateForecastWeatherUseCase> {
        UpdateForecastWeatherUseCase(
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