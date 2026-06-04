package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveFavoritesForecastsUseCase {
    operator fun invoke(request: List<Int>): Flow<List<FavoriteForecast>>
}

class ObserveFavoritesForecastsUseCaseImpl(
    private val repository: ForecastRepository,
) : ObserveFavoritesForecastsUseCase {

    override fun invoke(request: List<Int>): Flow<List<FavoriteForecast>> =
        repository.observeFavoriteForecasts(request)

}