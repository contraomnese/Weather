package com.contraomnese.weather.domain.cleanarchitecture.usecase.withoutRequest

fun interface UseCase<RESULT> {
    suspend operator fun invoke(onResult: (RESULT) -> Unit)
}