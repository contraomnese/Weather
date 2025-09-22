package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCaseWithRequest
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.flow.Flow

class ObserveForecastWeatherUseCase(
    private val repository: ForecastWeatherRepository,
) : StreamingUseCaseWithRequest<LocationInfoDomainModel, ForecastWeatherDomainModel?> {

    override fun invoke(request: LocationInfoDomainModel): Flow<ForecastWeatherDomainModel?> = repository.observeBy(request)

}