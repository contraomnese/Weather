package com.contraomnese.weather.domain.cleanarchitecture.usecase.withoutRequest

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import kotlinx.coroutines.withContext

abstract class BackgroundExecutingUseCase<RESULT>(
    private val coroutineContextProvider: CoroutineContextProvider
) : UseCase<RESULT> {
    final override suspend fun invoke(
        onResult: (RESULT) -> Unit
    ) {
        val result = withContext(coroutineContextProvider.io) {
            executeInBackground()
        }
        onResult(result)
    }

    internal abstract fun executeInBackground(): RESULT
}