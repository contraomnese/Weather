package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.cleanarchitecture.usecase.withRequest.BackgroundExecutingUseCaseWithRequest
import com.contraomnese.weather.domain.home.model.MyLocationDomainModel
import com.contraomnese.weather.domain.home.repository.MyLocationsRepository

class GetMyLocationUseCase(
    private val repository: MyLocationsRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
) : BackgroundExecutingUseCaseWithRequest<String, MyLocationDomainModel>(coroutineContextProvider) {

    override fun executeInBackground(request: String): MyLocationDomainModel = repository.getLocationBy(request)

}