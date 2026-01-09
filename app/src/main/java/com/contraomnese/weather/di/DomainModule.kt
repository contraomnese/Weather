package com.contraomnese.weather.di

import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastsWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastsWeatherUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCaseImpl
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsUseCase> {
        GetLocationsUseCaseImpl(
            repository = get()
        )
    }
    factory<GetLocationUseCase> {
        GetLocationUseCaseImpl(
            repository = get()
        )
    }
    factory<AddFavoriteUseCase> {
        AddFavoriteUseCaseImpl(
            repository = get()
        )
    }
    factory<RemoveFavoriteUseCase> {
        RemoveFavoriteUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveFavoritesUseCase> {
        ObserveFavoritesUseCaseImpl(
            repository = get()
        )
    }
    factory<GetFavoritesUseCase> {
        GetFavoritesUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveForecastWeatherUseCase> {
        ObserveForecastWeatherUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveForecastsWeatherUseCase> {
        ObserveForecastsWeatherUseCaseImpl(
            repository = get()
        )
    }
    factory<UpdateForecastWeatherUseCase> {
        UpdateForecastWeatherUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveAppSettingsUseCase> {
        ObserveAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<UpdateAppSettingsUseCase> {
        UpdateAppSettingsUseCaseImpl(
            repository = get()
        )
    }
}