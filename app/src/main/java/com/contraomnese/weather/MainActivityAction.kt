package com.contraomnese.weather

import com.contraomnese.weather.presentation.architecture.MviAction

internal sealed interface MainActivityAction : MviAction {
    data class AddFavorite(val locationId: Int) : MainActivityAction
    data object NotLoading : MainActivityAction
}