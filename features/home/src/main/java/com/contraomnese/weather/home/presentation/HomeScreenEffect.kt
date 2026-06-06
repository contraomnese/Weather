package com.contraomnese.weather.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.MviEffect
import kotlinx.datetime.Instant

internal sealed interface HomeScreenEffect : MviEffect {
    data class InputLocationUpdated(val input: TextFieldValue) : HomeScreenEffect
    data class MatchingLocationsUpdated(val locations: List<Location>) : HomeScreenEffect
    data class GpsLocationUpdated(val location: Location) : HomeScreenEffect
    data class FavoritesUpdated(
//        val locations: List<Location>,
        val forecasts: Map<Location, FavoriteForecast>,
    ) : HomeScreenEffect
    data class AccessFineLocationPermissionGranted(val isGranted: Boolean) : HomeScreenEffect
    data class GpsModeEnabled(val isEnabled: Boolean) : HomeScreenEffect
    data class CurrentTimeUpdated(val time: Instant) : HomeScreenEffect
}