package com.contraomnese.weather.presentation.coroutine

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers

class AppCoroutineContextProvider : CoroutineContextProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
}