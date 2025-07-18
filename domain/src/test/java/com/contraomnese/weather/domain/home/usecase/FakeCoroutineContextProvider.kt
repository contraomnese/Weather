package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers

object FakeCoroutineContextProvider : CoroutineContextProvider {
    override val main = Dispatchers.Unconfined
    override val io = Dispatchers.Unconfined
}