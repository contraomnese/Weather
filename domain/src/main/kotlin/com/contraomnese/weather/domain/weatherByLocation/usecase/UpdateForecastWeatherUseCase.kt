package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository

fun interface UpdateForecastWeatherUseCase {
    suspend operator fun invoke(request: Int): Result<Int>
}

class UpdateForecastWeatherUseCaseImpl(
    private val repository: ForecastRepository,
) : UpdateForecastWeatherUseCase {

    override suspend fun invoke(request: Int) =
        repository.refreshForecastByLocationId(request)

}