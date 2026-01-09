package com.contraomnese.weather.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.MviAction

internal sealed interface HomeScreenAction : MviAction {
    data class InputLocation(val input: TextFieldValue) : HomeScreenAction
    data class AddFavorite(val locationId: Int) : HomeScreenAction
    data class RemoveFavorite(val locationId: Int) : HomeScreenAction
    data class UpdateGpsLocation(val lat: Double, val lon: Double) : HomeScreenAction
    data class SwitchGpsMode(val enabled: Boolean) : HomeScreenAction
    data class AccessFineLocationPermissionGranted(val granted: Boolean) : HomeScreenAction
    data class DeviceGpsModeEnabled(val enabled: Boolean) : HomeScreenAction
    data class UpdateFavorites(val favorites: List<Location>) : HomeScreenAction
}