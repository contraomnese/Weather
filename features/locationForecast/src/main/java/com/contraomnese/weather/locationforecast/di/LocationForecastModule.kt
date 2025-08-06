package com.contraomnese.weather.locationforecast.di

import com.contraomnese.weather.locationforecast.presentation.LocationForecastViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val locationForecastModule = module {

    viewModel { params ->

        val id = params.get<Int>()

        LocationForecastViewModel(
            useCaseExecutorProvider = get(),
            notificationMonitor = get(),
            getLocationForecastUseCase = get(),
            getCurrentWeatherUseCase = get(),
            locationId = id
        )
    }
}