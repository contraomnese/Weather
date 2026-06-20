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
internal data class SearchByGpsData(
    val accessFineLocationPermissionGranted: Boolean = false,
    val location: Location? = null,
)

@Immutable
internal data class HomeScreenState(
    override val isLoading: Boolean = false,
    val isSearchingByUserInput: Boolean = false,
    val isSearchingByGpsMode: Boolean = false,
    val userInputToSearchBar: TextFieldValue = TextFieldValue(),
    val searchByGpsModeData: SearchByGpsData = SearchByGpsData(),
    val matchingLocations: ImmutableList<Location> = persistentListOf(),
    val favorites: Favorites = persistentMapOf(),
    val currentTime: Instant = Clock.System.now(),
    val isPushNotificationEnabled: Boolean = true,
) : MviState {

    private fun cleanFavorites(): HomeScreenState = copy(
        isLoading = false,
        isSearchingByUserInput = false,
        favorites = persistentMapOf()
    )

    fun setInputLocation(newValue: TextFieldValue): HomeScreenState {
        return copy(
            userInputToSearchBar = newValue,
            matchingLocations = if (newValue.text.isEmpty()) persistentListOf() else matchingLocations,
            isSearchingByUserInput = newValue.text.isNotEmpty()
        )
    }

    fun setGpsLocation(location: Location): HomeScreenState =
        copy(searchByGpsModeData = searchByGpsModeData.copy(location = location))

    fun setMatchingLocations(locations: List<Location>): HomeScreenState =
        copy(matchingLocations = locations.toPersistentList(), isSearchingByUserInput = false)

    fun setFavorites(
        newFavorites: Map<Location, FavoriteForecast>,
    ): HomeScreenState =
        if (newFavorites.isEmpty())
            cleanFavorites()
        else
            copy(
                isLoading = false,
                isSearchingByUserInput = false,
                favorites = newFavorites.toPersistentMap(),
            )

    fun setAccessFineLocationPermissionGranted(isGranted: Boolean): HomeScreenState =
        copy(searchByGpsModeData = searchByGpsModeData.copy(accessFineLocationPermissionGranted = isGranted))

    fun setTime(time: Instant): HomeScreenState =
        copy(currentTime = time)

    fun setPushNotificationsEnabled(isEnabled: Boolean): HomeScreenState =
        this.copy(isPushNotificationEnabled = isEnabled)

    companion object {
        val DEFAULT = HomeScreenState(isLoading = true)
    }
}

internal fun TextFieldValue.hasValidLocation(): Boolean {
    val locationRegex = "^[\\p{L}\\s\\-'.,]*\$".toRegex()
    return this.text.matches(locationRegex)
}