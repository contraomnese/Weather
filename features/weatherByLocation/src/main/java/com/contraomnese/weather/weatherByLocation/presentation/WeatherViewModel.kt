package com.contraomnese.weather.weatherByLocation.presentation

import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.notInitialize
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

internal class WeatherViewModel(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val navLocationId: Int,
) : MviModel<WeatherScreenAction, WeatherScreenEffect, WeatherScreenEvent, WeatherScreenState>(
    defaultState = WeatherScreenState.default(navLocationId),
    tag = "WeatherViewModel"
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun bootstrap() {
        stateFlow
            .map { it.locationId }
            .distinctUntilChanged()
            .flatMapLatest { locationId ->
                combine(
                    observeForecastWeatherUseCase(locationId),
                    observeAppSettingsUseCase(),
                    observeFavoritesUseCase()
                ) { weather, appSettings, favorites ->
                    weather?.let {
                        push(
                            WeatherScreenEffect.InitialUpdated(
                                weather = weather,
                                appSettings = appSettings,
                                favorites = favorites
                            )
                        )
                    } ?: updateForecastWeatherUseCase(locationId)
                }
                    .catch {
                        push(WeatherScreenEvent.HandleError(notInitialize(logPrefix("Bootstrap failed"), it)))
                    }
            }
            .launchIn(
                scope = viewModelScope
            )
    }

    override fun reducer(
        effect: WeatherScreenEffect,
        previousState: WeatherScreenState,
    ) = when (effect) {
        is WeatherScreenEffect.FavoritesUpdated -> previousState.setFavorites(effect.favorites)
        is WeatherScreenEffect.WeatherUpdated -> previousState.setWeather(effect.weather)
        is WeatherScreenEffect.InitialUpdated -> previousState.init(effect.weather, effect.appSettings, effect.favorites)
        is WeatherScreenEffect.LocationUpdated -> previousState.setLocationId(effect.locationId)
        is WeatherScreenEffect.AppSettingsUpdated -> previousState.setAppSettings(effect.appSettings)
    }

    override suspend fun actor(action: WeatherScreenAction) = when (action) {
        is WeatherScreenAction.NavigateToHome -> push(WeatherScreenEvent.NavigateToHome)
        is WeatherScreenAction.SwapFavorite -> processSwapFavorite(action.index)
        is WeatherScreenAction.AddFavorite -> processFavoriteAdd(action.locationId)
    }

    private fun processSwapFavorite(index: Int) {
        val currentLocationId = stateFlow.value.favorites[index].id
        if (currentLocationId != stateFlow.value.locationId)
            push(WeatherScreenEffect.LocationUpdated(currentLocationId))
    }

    private suspend fun processFavoriteAdd(locationId: Int) {
        addFavoriteUseCase(locationId)
            .onFailure { push(WeatherScreenEvent.HandleError(it)) }
    }
}