package com.contraomnese.weather.presentation.usecase

import com.contraomnese.weather.domain.cleanarchitecture.usecase.UseCaseExecutor
import kotlinx.coroutines.CoroutineScope

typealias UseCaseExecutorProvider =
        @JvmSuppressWildcards (coroutineScope: CoroutineScope) -> UseCaseExecutor