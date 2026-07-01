package com.contraomnese.weather.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.app.usecase.DisableFavoritesForecastPushNotificationUseCase
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.FavoriteForecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveFavoriteForecastsUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateFavoritesForecastsUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastUseCase
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class HomeViewModel(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val observeFavoriteForecastsUseCase: ObserveFavoriteForecastsUseCase,
    private val updateForecastUseCase: UpdateForecastUseCase,
    private val updateFavoritesForecastsUseCase: UpdateFavoritesForecastsUseCase,
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val disablePushNotificationUseCase: DisableFavoritesForecastPushNotificationUseCase,
) : MviModel<HomeScreenAction, HomeScreenEffect, HomeScreenEvent, HomeScreenState>(
    defaultState = HomeScreenState.DEFAULT,
    tag = "HomeViewModel",
) {

    companion object {
        private const val RUN_SEARCHING_DELAY = 2000L
    }

    init {
        viewModelScope.launch {
            flow {
                while (true) {
                    val now = Clock.System.now()
                    emit(now)
                    delay(
                        (60 - now.toLocalDateTime(TimeZone.currentSystemDefault()).second) * 1000L
                    )
                }
            }
                .collect {
                    push(HomeScreenEffect.CurrentTimeUpdated(it))
                }
        }
    }

    override suspend fun bootstrap() {
        viewModelScope.launch {
            observeAppSettingsUseCase()
                .collectLatest { settings ->
                    push(HomeScreenEffect.PushNotificationsEnabled(settings.favoritesForecastNotificationEnabled))
                }
        }

        viewModelScope.launch {
            observeFavoritesUseCase()
                .collectLatest { favorites ->
                    if (favorites.isEmpty()) {
                        push(HomeScreenAction.StopLoading)
                    } else {
                        push(HomeScreenAction.UpdateFavorites(favorites))
                    }

                }
        }
    }

    override fun reducer(effect: HomeScreenEffect, previousState: HomeScreenState): HomeScreenState = when (effect) {
        is HomeScreenEffect.InputLocationUpdated,
            -> previousState.setInputLocation(effect.input)

        is HomeScreenEffect.GpsLocationUpdated,
            -> previousState.setGpsLocation(effect.location)

        is HomeScreenEffect.MatchingLocationsUpdated,
            -> previousState.setMatchingLocations(effect.locations)

        is HomeScreenEffect.FavoritesUpdated,
            -> previousState.setFavorites(effect.forecasts)

        is HomeScreenEffect.AccessFineLocationPermissionGranted,
            -> previousState.setAccessFineLocationPermissionGranted(effect.isGranted)

        is HomeScreenEffect.CurrentTimeUpdated,
            -> previousState.setTime(effect.time)

        is HomeScreenEffect.PushNotificationsEnabled,
            -> previousState.setPushNotificationsEnabled(effect.isEnabled)

        HomeScreenEffect.StopLoading,
            -> previousState.setLoading(false)
    }

    override suspend fun actor(action: HomeScreenAction) = when (action) {

        is HomeScreenAction.InputLocation,
            -> processLocationInput(action.input)

        is HomeScreenAction.UpdateGpsLocation,
            -> processGpsLocationChange(LocationCoordinates.from(action.lat, action.lon))

        is HomeScreenAction.AccessFineLocationPermissionGranted,
            -> processAccessFineLocationPermissionGranted(action.granted)

        is HomeScreenAction.DeviceGpsModeEnabled,
            -> processGpsModeEnabled(action.enabled)

        is HomeScreenAction.AddFavorite,
            -> processFavoriteAdd(action.locationId)

        is HomeScreenAction.RemoveFavorite,
            -> processFavoriteRemove(action.locationId)

        is HomeScreenAction.UpdateFavorites,
            -> processFavoritesUpdate(action.favorites)

        HomeScreenAction.DisablePushNotification,
            -> processDisablePushNotification()

        HomeScreenAction.StopLoading -> processStopLoading()
    }

    private suspend fun processLocationInput(input: TextFieldValue) {

        push(HomeScreenEffect.InputLocationUpdated(input))
        if (input.text.isNotEmpty()) {
            delay(RUN_SEARCHING_DELAY)
            getLocationsUseCase(input.text)
                .onFailure { push(HomeScreenEvent.HandleError(it)) }
                .onSuccess {
                    push(HomeScreenEffect.MatchingLocationsUpdated(it))
                }
        }
    }

    private suspend fun processFavoriteAdd(locationId: Int) {
        addFavoriteUseCase(locationId)
            .onFailure {
                push(HomeScreenEvent.HandleError(it))
            }
    }

    private suspend fun processFavoriteRemove(locationId: Int) {
        removeFavoriteUseCase(locationId)
            .onFailure {
                push(HomeScreenEvent.HandleError(it))
            }
    }

    private suspend fun processFavoritesUpdate(newFavoriteLocations: List<Location>) {

        val newFavoriteLocationIds = newFavoriteLocations.map { it.id }
        observeFavoriteForecastsUseCase(newFavoriteLocationIds)
            .collectLatest { newFavoriteForecasts ->

                if (newFavoriteForecasts.isEmpty()) {
                    val updatedFavorites = updateFavoritesForecastsUseCase(newFavoriteLocationIds)

                    updatedFavorites.onEachIndexed { idx, result ->
                        result
                            .onFailure {
                                val notUpdatedLocationId = newFavoriteLocations[idx].id
                                push(HomeScreenAction.RemoveFavorite(notUpdatedLocationId))
                                push(HomeScreenEvent.HandleError(it))
                            }
                    }

                    return@collectLatest
                }

                val forecastsById: Map<Int, FavoriteForecast> = newFavoriteForecasts.associateBy { it.locationId }
                val forecasts: Map<Location, FavoriteForecast> = newFavoriteLocations
                    .mapNotNull { location ->
                        val forecast = forecastsById[location.id]
                        if (forecast != null) {
                            location to forecast
                        } else {
                            null
                        }
                    }
                    .toMap()

                push(HomeScreenEffect.FavoritesUpdated(forecasts = forecasts))
            }
    }

    private suspend fun processDisablePushNotification() {
        disablePushNotificationUseCase()
            .onSuccess { push(HomeScreenEffect.PushNotificationsEnabled(false)) }
            .onFailure { push(HomeScreenEvent.HandleError(it)) }
    }

    private suspend fun processAccessFineLocationPermissionGranted(isGranted: Boolean) {
        delay(1000)
        push(HomeScreenEffect.AccessFineLocationPermissionGranted(isGranted))
    }

    private fun processGpsModeEnabled(isEnabled: Boolean) {
        if (isEnabled) push(HomeScreenEvent.GetGpsLocation)
    }

    private suspend fun processGpsLocationChange(coordinates: LocationCoordinates) {
        getLocationUseCase(coordinates)
            .onFailure { push(HomeScreenEvent.HandleError(it)) }
            .onSuccess {
                push(HomeScreenEffect.GpsLocationUpdated(it))
                push(HomeScreenEvent.NavigateToGpsLocation(it.id))
            }
    }

    private fun processStopLoading() {
        push(HomeScreenEffect.StopLoading)
    }

}