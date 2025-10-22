package com.contraomnese.weather

import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.MviEffect

internal sealed interface MainActivityEffect : MviEffect {
    data object NotLoading : MainActivityEffect
    data class FavoritesUpdated(val favorites: List<Location>) : MainActivityEffect
}