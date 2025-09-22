package com.contraomnese.weather.weatherByLocation.di

import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val weatherModule = module {

    viewModel { params ->

        val id = params.get<Int>(0)
        val lat = params.get<Double>(1)
        val lot = params.get<Double>(2)

        WeatherViewModel(
            useCaseExecutorProvider = get(),
            notificationMonitor = get(),
            updateForecastWeatherUseCase = get(),
            observeForecastWeatherUseCase = get(),
            getAppSettingsUseCase = get(),
            locationId = id,
            lat = lat,
            lot = lot
        )
    }
}