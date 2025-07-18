package com.contraomnese.weather.domain.cleanarchitecture.usecase.withRequest


fun interface UseCaseWithRequest<REQUEST, RESULT> {
    suspend operator fun invoke(input: REQUEST, onResult: (RESULT) -> Unit)
}