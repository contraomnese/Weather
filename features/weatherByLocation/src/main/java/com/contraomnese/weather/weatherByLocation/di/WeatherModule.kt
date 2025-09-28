package com.contraomnese.weather.weatherByLocation.di

import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel
import com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val weatherModule = module {

    viewModel { params ->

        val (id: Int, name: String, lat: Double, lot: Double) = params

        val location = LocationInfoDomainModel(
            id = id,
            name = name,
            point = CoordinatesDomainModel(
                latitude = LatitudeDomainModel(lat),
                longitude = LongitudeDomainModel(lot)
            )
        )

        WeatherViewModel(
            useCaseExecutorProvider = get(),
            notificationMonitor = get(),
            updateForecastWeatherUseCase = get(),
            observeForecastWeatherUseCase = get(),
            getAppSettingsUseCase = get(),
            observeFavoritesUseCase = get(),
            location = location
        )
    }
}