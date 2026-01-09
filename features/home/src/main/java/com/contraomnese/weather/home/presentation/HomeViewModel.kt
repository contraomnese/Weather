package com.contraomnese.weather.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastsWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
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
    private val observeForecastsWeatherUseCase: ObserveForecastsWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
) : MviModel<HomeScreenAction, HomeScreenEffect, HomeScreenEvent, HomeScreenState>(
    defaultState = HomeScreenState.DEFAULT,
    tag = "HomeViewModel",
) {

    companion object {
        private const val LOCATION_UPDATE_DELAY = 2000L
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
            }.collect {
                push(HomeScreenEffect.CurrentTimeUpdated(it))
            }
        }
    }

    override suspend fun bootstrap() {
        delay(400)
        observeFavoritesUseCase()
            .collectLatest { favorites ->
                push(HomeScreenEffect.FavoritesUpdated(favorites))
                push(HomeScreenAction.UpdateFavorites(favorites))
            }
    }

    override fun reducer(effect: HomeScreenEffect, previousState: HomeScreenState): HomeScreenState = when (effect) {
        is HomeScreenEffect.InputLocationUpdated -> previousState.setInputLocation(effect.input)
        is HomeScreenEffect.GpsLocationUpdated -> previousState.setGpsLocation(effect.location)
        is HomeScreenEffect.MatchingLocationsUpdated -> previousState.setMatchingLocations(effect.locations)
        is HomeScreenEffect.FavoritesUpdated -> previousState.setFavorites(effect.favorites)
        is HomeScreenEffect.FavoritesForecastUpdated -> previousState.setFavoritesForecast(effect.favoritesForecast)
        is HomeScreenEffect.AccessFineLocationPermissionGranted -> previousState.setAccessFineLocationPermissionGranted(effect.isGranted)
        is HomeScreenEffect.GpsModeEnabled -> previousState.setGpsModeEnabled(effect.isEnabled)
        is HomeScreenEffect.CurrentTimeUpdated -> previousState.setTime(effect.time)
    }

    override suspend fun actor(action: HomeScreenAction) = when (action) {
        is HomeScreenAction.InputLocation -> processLocationInput(action.input)
        is HomeScreenAction.UpdateGpsLocation -> processGpsLocationChange(LocationCoordinates.from(action.lat, action.lon))
        is HomeScreenAction.AddFavorite -> processFavoriteAdd(action.locationId)
        is HomeScreenAction.RemoveFavorite -> processFavoriteRemove(action.locationId)
        is HomeScreenAction.SwitchGpsMode -> processGpsModeEnabled(action.enabled)
        is HomeScreenAction.AccessFineLocationPermissionGranted -> processAccessFineLocationPermissionGranted(action.granted)
        is HomeScreenAction.DeviceGpsModeEnabled -> processGpsModeEnabled(action.enabled)
        is HomeScreenAction.UpdateFavorites -> processFavoritesUpdate(action.favorites)
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
            .onSuccess {
                push(HomeScreenEffect.GpsLocationUpdated(it))
                push(HomeScreenEvent.NavigateToGpsLocation(it.id))
            }

    }

    private suspend fun processFavoriteAdd(locationId: Int) {
        addFavoriteUseCase(locationId)
            .onSuccess { updateForecastWeatherUseCase(it) }
            .onFailure { push(HomeScreenEvent.HandleError(it)) }
    }

    private suspend fun processFavoriteRemove(locationId: Int) {
        removeFavoriteUseCase(locationId)
            .onFailure {
                push(HomeScreenEvent.HandleError(it))
            }
    }

    private suspend fun processAccessFineLocationPermissionGranted(isGranted: Boolean) {
        delay(1000)
        push(HomeScreenEffect.AccessFineLocationPermissionGranted(isGranted))
    }

    private fun processGpsModeEnabled(isEnabled: Boolean) {
        push(HomeScreenEffect.GpsModeEnabled(isEnabled))
        if (isEnabled) push(HomeScreenEvent.GetGpsLocation)
    }

    private suspend fun processFavoritesUpdate(favorites: List<Location>) {
        observeForecastsWeatherUseCase(favorites.map { it.id })
            .collectLatest { forecasts ->
                push(HomeScreenEffect.FavoritesForecastUpdated(forecasts.associateBy { it.location.id }))
            }
    }
}