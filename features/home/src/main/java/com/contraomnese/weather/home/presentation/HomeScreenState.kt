package com.contraomnese.weather.home.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.MviState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


typealias Favorites = ImmutableMap<Location, FavoriteForecast>

@Immutable
internal data class Gps(
    val location: Location? = null,
    val isPermissionGranted: Boolean = false,
    val isGpsMode: Boolean = false,
)

@Immutable
internal data class HomeScreenState(
    override val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val inputLocation: TextFieldValue = TextFieldValue(),
    val gps: Gps = Gps(),
    val matchingLocations: ImmutableList<Location> = persistentListOf(),
    val favorites: Favorites = persistentMapOf(),
    val currentTime: Instant = Clock.System.now(),
) : MviState {

    private fun cleanFavorites(): HomeScreenState = copy(
        isLoading = false,
        isSearching = false,
        favorites = persistentMapOf()
    )

    fun setInputLocation(newValue: TextFieldValue): HomeScreenState {
        return copy(
            inputLocation = newValue,
            matchingLocations = if (newValue.text.isEmpty()) persistentListOf() else matchingLocations,
            isSearching = newValue.text.isNotEmpty()
        )
    }

    fun setGpsLocation(location: Location): HomeScreenState =
        copy(gps = gps.copy(location = location, isGpsMode = false))

    fun setMatchingLocations(locations: List<Location>): HomeScreenState =
        copy(matchingLocations = locations.toPersistentList(), isSearching = false)

    fun setFavorites(
        newFavorites: Map<Location, FavoriteForecast>,
    ): HomeScreenState =
        if (newFavorites.isEmpty())
            cleanFavorites()
        else
            copy(
                isLoading = false,
                isSearching = false,
                favorites = newFavorites.toPersistentMap(),
            )

    fun setAccessFineLocationPermissionGranted(isGranted: Boolean): HomeScreenState =
        copy(gps = gps.copy(isPermissionGranted = isGranted))

    fun setGpsModeEnabled(isEnabled: Boolean): HomeScreenState =
        copy(gps = gps.copy(isGpsMode = isEnabled))

    fun setTime(time: Instant): HomeScreenState =
        copy(currentTime = time)

    companion object {
        val DEFAULT = HomeScreenState(isLoading = true)
    }
}

internal fun TextFieldValue.hasValidLocation(): Boolean {
    val locationRegex = "^[\\p{L}\\s\\-'.,]*\$".toRegex()
    return this.text.matches(locationRegex)
}