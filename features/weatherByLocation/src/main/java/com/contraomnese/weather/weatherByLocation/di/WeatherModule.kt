package com.contraomnese.weather.weatherByLocation.di

import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val weatherModule
    get() = module {

        viewModel { params ->

            val locationId: Int = params.get()

            WeatherViewModel(
                updateForecastWeatherUseCase = get(),
                observeForecastWeatherUseCase = get(),
                observeAppSettingsUseCase = get(),
                observeFavoritesUseCase = get(),
                addFavoriteUseCase = get(),
                navLocationId = locationId
            )
        }
    }