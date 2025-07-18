package com.contraomnese.weather.domain.cleanarchitecture.usecase.withRequest

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import kotlinx.coroutines.withContext

abstract class BackgroundExecutingUseCaseWithRequest<REQUEST, RESULT>(
    private val coroutineContextProvider: CoroutineContextProvider
) : UseCaseWithRequest<REQUEST, RESULT> {
    final override suspend operator fun invoke(
        input: REQUEST,
        onResult: (RESULT) -> Unit
    ) {
        val result = withContext(coroutineContextProvider.io) {
            executeInBackground(input)
        }
        onResult(result)
    }

    internal abstract fun executeInBackground(
        request: REQUEST
    ): RESULT
}