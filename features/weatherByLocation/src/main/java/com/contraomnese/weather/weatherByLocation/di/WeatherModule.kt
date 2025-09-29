package com.contraomnese.weather.weatherByLocation.di

import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val weatherModule = module {

    viewModel { params ->

        val locationId: Int = params.get()

        WeatherViewModel(
            useCaseExecutorProvider = get(),
            notificationMonitor = get(),
            updateForecastWeatherUseCase = get(),
            observeForecastWeatherUseCase = get(),
            getAppSettingsUseCase = get(),
            observeFavoritesUseCase = get(),
            navLocationId = locationId
        )
    }
}