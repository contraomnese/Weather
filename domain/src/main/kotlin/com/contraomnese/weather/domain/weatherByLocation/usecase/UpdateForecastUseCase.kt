package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository

fun interface UpdateForecastUseCase {
    suspend operator fun invoke(request: Int): Result<Int>
}

class UpdateForecastUseCaseImpl(
    private val repository: ForecastRepository,
) : UpdateForecastUseCase {

    override suspend fun invoke(request: Int) =
        repository.updateForecastByLocationId(request)

}