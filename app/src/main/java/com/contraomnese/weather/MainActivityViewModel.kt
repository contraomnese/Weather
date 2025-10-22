package com.contraomnese.weather

import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn


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
            .stateIn(viewModelScope)
    }

    override fun reducer(effect: MainActivityEffect, previousState: MainActivityState): MainActivityState = when (effect) {
        is MainActivityEffect.NotLoading -> previousState.copy(isLoading = false)
        is MainActivityEffect.FavoritesUpdated -> previousState.setFavorites(effect.favorites)
    }

    override suspend fun actor(action: MainActivityAction) = when (action) {
        is MainActivityAction.AddFavorite -> processFavoriteAdd(action.locationId)
        MainActivityAction.NotLoading -> push(MainActivityEffect.NotLoading)
    }

    private suspend fun processFavoriteAdd(locationId: Int) {
        addFavoriteUseCase(locationId)
    }
}