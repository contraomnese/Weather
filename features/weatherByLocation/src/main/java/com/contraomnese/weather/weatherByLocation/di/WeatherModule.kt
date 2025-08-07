package com.contraomnese.weather.weatherByLocation.di

import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val weatherModule = module {

    viewModel { params ->

        val id = params.get<Int>()

        WeatherViewModel(
            useCaseExecutorProvider = get(),
            notificationMonitor = get(),
            getGeoLocationUseCase = get(),
            getCurrentWeatherUseCase = get(),
            locationId = id
        )
    }
}