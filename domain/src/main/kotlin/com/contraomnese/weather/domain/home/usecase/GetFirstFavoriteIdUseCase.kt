package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository

fun interface GetFirstFavoriteIdUseCase {
    operator fun invoke(): Int?
}

class GetFirstFavoriteIdUseCaseImpl(
    private val repository: LocationsRepository,
) : GetFirstFavoriteIdUseCase {
    override fun invoke(): Int? = repository.getFirstFavoriteId()
}