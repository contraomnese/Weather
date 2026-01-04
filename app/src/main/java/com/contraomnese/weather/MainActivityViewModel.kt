package com.contraomnese.weather

import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.exceptions.notInitialize
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.presentation.architecture.MviModel
import com.contraomnese.weather.weatherByLocation.navigation.WeatherByLocationDestination
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


internal class MainActivityViewModel(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
) : MviModel<MainActivityAction, MainActivityEffect, MainActivityEvent, MainActivityState>(
    tag = "MainActivity",
    defaultState = MainActivityState.DEFAULT
) {
    override suspend fun bootstrap() {
        observeFavoritesUseCase()
            .onEach {
                push(MainActivityEffect.FavoritesUpdated(it))
            }
            .catch {
                push(MainActivityEvent.HandleError(notInitialize(logPrefix("Bootstrap failed"), it)))
            }
            .launchIn(viewModelScope)
    }

    override fun reducer(effect: MainActivityEffect, previousState: MainActivityState): MainActivityState = when (effect) {
        is MainActivityEffect.NotLoading -> previousState.copy(
            isLoading = false,
            startDestination = previousState.favorites.firstOrNull()?.let {
                WeatherByLocationDestination(id = it.id)
            } ?: HomeDestination)
        is MainActivityEffect.FavoritesUpdated -> previousState.setFavorites(effect.favorites)
    }

    override suspend fun actor(action: MainActivityAction) = when (action) {
        is MainActivityAction.AddFavorite -> processFavoriteAdd(action.locationId)
        is MainActivityAction.LottieAnimationFinished -> push(MainActivityEffect.NotLoading)
    }

    private suspend fun processFavoriteAdd(locationId: Int) {
        addFavoriteUseCase(locationId)
            .onFailure { cause -> push(MainActivityEvent.HandleError(cause)) }
    }
}