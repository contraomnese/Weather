package com.contraomnese.weather

import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.notInitialize
import com.contraomnese.weather.domain.home.usecase.GetFirstFavoriteIdUseCase
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MainActivityViewModel(
    private val getFirstFavoriteIdUseCase: GetFirstFavoriteIdUseCase,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
) : MviModel<MainActivityAction, MainActivityEffect, MainActivityEvent, MainActivityState>(
    tag = "MainActivity",
    defaultState = MainActivityState.DEFAULT
) {
    override suspend fun bootstrap() {
        push(MainActivityEffect.WeatherDestinationIdChanged(getFirstFavoriteIdUseCase()))

        observeAppSettingsUseCase()
            .onEach {
                push(MainActivityEffect.ForecastAutoSyncChanged(it.forecastAutoSync))
            }
            .catch {
                push(MainActivityEvent.HandleError(notInitialize(logPrefix("Bootstrap failed"), it)))
            }
            .launchIn(viewModelScope)
    }

    override fun reducer(effect: MainActivityEffect, previousState: MainActivityState): MainActivityState = when (effect) {
        is MainActivityEffect.LoadingFinished -> previousState.finishLoading()
        is MainActivityEffect.ForecastAutoSyncChanged -> previousState.setForecastAutoSync(effect.enabled)
        is MainActivityEffect.WeatherDestinationIdChanged -> previousState.setWeatherDestinationId(effect.id)
    }
}