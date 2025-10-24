package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository

fun interface UpdateForecastWeatherUseCase {
    suspend operator fun invoke(request: Int): Result<Unit>
}

class UpdateForecastWeatherUseCaseImpl(
    private val repository: ForecastWeatherRepository,
) : UpdateForecastWeatherUseCase {

    override suspend fun invoke(request: Int): Result<Unit> =
        repository.updateBy(request)

}