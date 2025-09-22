package com.contraomnese.weather.di

import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsInfoUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsInfoUseCase> {
        GetLocationsInfoUseCase(
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
    factory<GetFavoritesUseCase> {
        GetFavoritesUseCase(
            repository = get(),
            coroutineContextProvider = get()
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