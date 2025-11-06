package com.contraomnese.weather.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.cleanarchitecture.exception.logPrefix
import com.contraomnese.weather.domain.cleanarchitecture.exception.notInitialize
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import com.contraomnese.weather.presentation.architecture.MviModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class HomeViewModel(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
) : MviModel<HomeScreenAction, HomeScreenEffect, HomeScreenEvent, HomeScreenState>(
    defaultState = HomeScreenState.DEFAULT,
    tag = "HomeViewModel",
) {

    companion object {
        private const val LOCATION_UPDATE_DELAY = 2000L
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun bootstrap() {
        observeFavoritesUseCase()
            .onEach {
                push(HomeScreenEffect.FavoritesUpdated(it))
            }
            .catch {
                push(HomeScreenEvent.HandleError(notInitialize(logPrefix("Can't observe favorites"), it)))
            }
            .launchIn(viewModelScope)

        stateFlow
            .map { it.favorites }
            .distinctUntilChanged()
            .flatMapLatest { newFavorites ->
                if (newFavorites.isEmpty()) {
                    emptyFlow()
                } else {
                    val forecastFlows = newFavorites.map { location ->
                        observeForecastWeatherUseCase(location.id)
                            .onEach { if (it == null) updateForecastWeatherUseCase(location.id) }
                            .filterNotNull()
                            .map { forecast -> location.id to forecast }
                            .catch {
                                push(HomeScreenEvent.HandleError(notInitialize(logPrefix("Can't observe forecast weather"), it)))
                            }
                    }
                    combine(forecastFlows) { pairs ->
                        pairs.toMap()
                    }
                }
            }
            .onEach { forecastsMap ->
                push(HomeScreenEffect.FavoritesForecastUpdated(forecastsMap))
            }
            .catch {
                push(HomeScreenEvent.HandleError(notInitialize(logPrefix("Bootstrap failed"), it)))
            }
            .launchIn(viewModelScope)
    }

    override fun reducer(effect: HomeScreenEffect, previousState: HomeScreenState): HomeScreenState = when (effect) {
        is HomeScreenEffect.InputLocationUpdated -> previousState.setInputLocation(effect.input)
        is HomeScreenEffect.GpsLocationUpdated -> previousState.setGpsLocation(effect.location)
        is HomeScreenEffect.MatchingLocationsUpdated -> previousState.setMatchingLocations(effect.locations)
        is HomeScreenEffect.FavoritesUpdated -> previousState.setFavorites(effect.favorites)
        is HomeScreenEffect.FavoritesForecastUpdated -> previousState.setFavoritesForecast(effect.favoritesForecast)
        is HomeScreenEffect.AccessFineLocationPermissionGranted -> previousState.setAccessFineLocationPermissionGranted(effect.isGranted)
        is HomeScreenEffect.GpsModeEnabled -> previousState.setGpsModeEnabled(effect.isEnabled)
    }

    override suspend fun actor(action: HomeScreenAction) = when (action) {
        is HomeScreenAction.InputLocation -> processLocationInput(action.input)
        is HomeScreenAction.UpdateGpsLocation -> processGpsLocationChange(LocationCoordinates.from(action.lat, action.lon))
        is HomeScreenAction.AddFavorite -> processFavoriteAdd(action.locationId)
        is HomeScreenAction.RemoveFavorite -> processFavoriteRemove(action.locationId)
        is HomeScreenAction.SwitchGpsMode -> processSwitchGpsMode(action.enabled)
        is HomeScreenAction.AccessFineLocationPermissionGranted -> processAccessFineLocationPermissionGranted(action.granted)
        is HomeScreenAction.DeviceGpsModeEnabled -> processGpsModeEnabled(action.enabled)
    }

    private suspend fun processLocationInput(input: TextFieldValue) {
        push(HomeScreenEffect.InputLocationUpdated(input))

        if (input.text.isNotEmpty()) {
            delay(LOCATION_UPDATE_DELAY)
            getLocationsUseCase(input.text)
                .onFailure { push(HomeScreenEvent.HandleError(it)) }
                .onSuccess { push(HomeScreenEffect.MatchingLocationsUpdated(it)) }
        }
    }

    private suspend fun processGpsLocationChange(coordinates: LocationCoordinates) {
        getLocationUseCase(coordinates)
            .onFailure { push(HomeScreenEvent.HandleError(it)) }
            .onSuccess { push(HomeScreenEffect.GpsLocationUpdated(it)) }

    }

    private suspend fun processFavoriteAdd(locationId: Int) {
        addFavoriteUseCase(locationId)
            .onFailure { push(HomeScreenEvent.HandleError(it)) }
    }

    private suspend fun processFavoriteRemove(locationId: Int) {
        removeFavoriteUseCase(locationId)
            .onFailure { push(HomeScreenEvent.HandleError(it)) }
    }

    private fun processSwitchGpsMode(enabled: Boolean) {
        push(HomeScreenEvent.SwitchGpsMode(enabled))
    }

    private suspend fun processAccessFineLocationPermissionGranted(isGranted: Boolean) {
        delay(1000)
        push(HomeScreenEffect.AccessFineLocationPermissionGranted(isGranted))
    }

    private fun processGpsModeEnabled(isEnabled: Boolean) {
        push(HomeScreenEffect.GpsModeEnabled(isEnabled))
        if (isEnabled) push(HomeScreenEvent.GetGpsLocation)
    }

}