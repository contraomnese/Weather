package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow

fun interface ObserveFavoriteForecastUseCase {
    operator fun invoke(request: Int): Flow<FavoriteForecast?>
}

class ObserveFavoriteForecastUseCaseImpl(
    private val repository: ForecastRepository,
) : ObserveFavoriteForecastUseCase {

    override fun invoke(request: Int): Flow<FavoriteForecast?> =
        repository.observeFavoriteForecast(request)

}