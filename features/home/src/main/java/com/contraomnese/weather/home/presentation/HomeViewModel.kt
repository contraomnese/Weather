package com.contraomnese.weather.home.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.home.model.InputLocationPresentation
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsInfoUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Immutable
internal data class HomeUiState(
    override val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val inputLocation: InputLocationPresentation = InputLocationPresentation(""),
    val gpsLocation: LocationInfoDomainModel? = null,
    val matchingLocations: ImmutableList<LocationInfoDomainModel> = persistentListOf(),
    val favorites: ImmutableList<LocationInfoDomainModel> = persistentListOf(),
    val favoritesForecast: ImmutableMap<Int, ForecastWeatherDomainModel> = persistentMapOf(),
) : UiState {
    override fun loading(): HomeUiState = copy(isLoading = true)
}

@Immutable
internal sealed interface HomeEvent {
    data class InputLocationChanged(val newInput: String) : HomeEvent
    data class GpsLocationChanged(val lat: Double, val lon: Double) : HomeEvent
    data class AddFavorite(val locationId: Int) : HomeEvent
    data class RemoveFavorite(val locationId: Int) : HomeEvent
}

internal class HomeViewModel(
    useCaseExecutorProvider: UseCaseExecutorProvider,
    notificationMonitor: NotificationMonitor,
    private val getLocationsInfoUseCase: GetLocationsInfoUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
) : BaseViewModel<HomeUiState, HomeEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(observeFavoritesUseCase, ::onFavoritesUpdate, ::provideException)
    }

    private var searchJob: Job? = null

    override fun initialState(): HomeUiState = HomeUiState(isLoading = true)

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.InputLocationChanged -> onInputLocationChanged(event.newInput)
            is HomeEvent.AddFavorite -> onFavoriteAdded(event.locationId)
            is HomeEvent.RemoveFavorite -> onFavoriteRemoved(event.locationId)
            is HomeEvent.GpsLocationChanged -> onGpsLocationChanged(
                CoordinatesDomainModel(
                    LatitudeDomainModel(event.lat),
                    LongitudeDomainModel(event.lon)
                )
            )
        }
    }

    private fun onInputLocationChanged(newLocation: String) {
        updateViewState { copy(inputLocation = InputLocationPresentation(newLocation)) }

        if (newLocation.isNotEmpty()) {
            updateLocationsWithDebounce(2000L) {
                execute(
                    getLocationsInfoUseCase,
                    newLocation,
                    ::onLocationsUpdated,
                    ::provideException
                )
            }
        } else onLocationsUpdated(persistentListOf())
    }

    private fun onGpsLocationChanged(coordinates: CoordinatesDomainModel) {
        execute(
            getLocationUseCase,
            coordinates,
            ::onGpsLocationUpdated,
            ::provideException
        )
    }

    private fun onFavoriteAdded(locationId: Int) {
        execute(
            addFavoriteUseCase,
            locationId,
            onException = ::provideException
        )
    }

    private fun onFavoriteRemoved(locationId: Int) {
        execute(
            removeFavoriteUseCase,
            locationId,
            onException = ::provideException
        )
    }

    private fun onLocationsUpdated(newCities: List<LocationInfoDomainModel>) {
        updateViewState { copy(matchingLocations = newCities.toPersistentList(), isSearching = false) }
    }

    private fun onGpsLocationUpdated(newLocation: LocationInfoDomainModel) {
        updateViewState { copy(gpsLocation = newLocation) }
    }

    private fun onFavoritesUpdate(newFavorites: List<LocationInfoDomainModel>) {

        if (newFavorites.isEmpty()) updateViewState {
            copy(
                isLoading = false,
                favorites = persistentListOf(),
                favoritesForecast = persistentMapOf()
            )
        }
        else {
            updateViewState { copy(favorites = newFavorites.toPersistentList()) }
            val missing = newFavorites.filter { it.id !in uiState.value.favoritesForecast.keys }

            var remaining = missing.size
            missing.forEach { location ->
                observe(
                    observeForecastWeatherUseCase,
                    location.id,
                    { forecast ->
                        onFavoritesForecastUpdate(forecast, location)
                        remaining -= 1
                        if (remaining == 0) {
                            updateViewState { copy(isLoading = false) }
                        }
                    },
                    ::provideException
                )
            }
        }
    }

    private fun onFavoritesForecastUpdate(newFavoritesForecast: ForecastWeatherDomainModel?, favorite: LocationInfoDomainModel) {

        if (newFavoritesForecast != null) {
            val favoritesForecast = uiState.value.favoritesForecast.toMutableMap()
            favoritesForecast[favorite.id] = newFavoritesForecast
            updateViewState { copy(favoritesForecast = favoritesForecast.toPersistentMap()) }
        } else {
            execute(updateForecastWeatherUseCase, favorite.id, onException = ::provideException)
        }
    }

    private fun updateLocationsWithDebounce(debounce: Long, action: () -> Unit) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            updateViewState { copy(isSearching = true) }
            delay(debounce)
            action()
        }
    }

}