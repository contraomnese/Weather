package com.contraomnese.weather.domain.cleanarchitecture.usecase

import kotlinx.coroutines.flow.Flow

fun interface UseCase<RESULT> {
    suspend operator fun invoke(): RESULT
}

fun interface UseCaseWithRequest<REQUEST, RESULT> {
    suspend operator fun invoke(input: REQUEST): RESULT
}

fun interface StreamingUseCase<RESULT> {
    operator fun invoke(): Flow<RESULT>
}