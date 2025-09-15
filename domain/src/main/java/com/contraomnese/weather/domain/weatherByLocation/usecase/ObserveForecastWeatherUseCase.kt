package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCaseWithRequest
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.flow.Flow

class ObserveForecastWeatherUseCase(
    private val repository: ForecastWeatherRepository,
) : StreamingUseCaseWithRequest<DetailsLocationDomainModel, ForecastWeatherDomainModel?> {

    override fun invoke(request: DetailsLocationDomainModel): Flow<ForecastWeatherDomainModel?> = repository.observeBy(request)

}