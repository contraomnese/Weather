package com.contraomnese.weather.home.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.home.model.LocationPresentation
import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
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
    val inputLocation: LocationPresentation = LocationPresentation(""),
    val matchingLocations: ImmutableList<MatchingLocationDomainModel> = persistentListOf(),
    val favorites: ImmutableList<DetailsLocationDomainModel> = persistentListOf(),
    val favoritesForecast: ImmutableMap<Int, ForecastWeatherDomainModel> = persistentMapOf(),
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface HomeEvent {
    data class LocationChanged(val newLocation: String) : HomeEvent
    data class AddFavorite(val locationId: Int) : HomeEvent
    data class RemoveFavorite(val locationId: Int) : HomeEvent
}

internal class HomeViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
) : BaseViewModel<HomeUiState, HomeEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(observeFavoritesUseCase, ::onFavoritesUpdate, ::provideException)
        viewModelScope.launch {
            uiState.collect {
                it.favorites.forEach { favorite ->
                    observe(
                        observeForecastWeatherUseCase,
                        favorite,
                        { forecast -> onFavoritesForecastUpdate(forecast, favorite) },
                        ::provideException
                    )
                }
            }
        }
    }

    private var searchJob: Job? = null

    override fun initialState(): HomeUiState = HomeUiState()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LocationChanged -> onLocationChanged(event.newLocation)
            is HomeEvent.AddFavorite -> onFavoriteAdded(event.locationId)
            is HomeEvent.RemoveFavorite -> onFavoriteRemoved(event.locationId)
        }
    }

    private fun onLocationChanged(newCity: String) {
        updateViewState { copy(inputLocation = LocationPresentation(newCity)) }

        if (newCity.isNotEmpty()) {
            updateLocationsWithDebounce(500L) {
                execute(
                    getLocationsUseCase,
                    newCity,
                    ::onCitiesUpdate,
                    ::provideException
                )
            }
        } else onCitiesUpdate(persistentListOf())
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

    private fun onCitiesUpdate(newCities: List<MatchingLocationDomainModel>) {
        updateViewState { copy(matchingLocations = newCities.toPersistentList(), isLoading = false) }
    }

    private fun onFavoritesUpdate(newFavorites: List<DetailsLocationDomainModel>) {
        updateViewState { copy(favorites = newFavorites.toPersistentList()) }
    }

    private fun onFavoritesForecastUpdate(newFavoritesForecast: ForecastWeatherDomainModel?, favorite: DetailsLocationDomainModel) {

        if (newFavoritesForecast != null) {
            val favoritesForecast = uiState.value.favoritesForecast.toMutableMap()
            favoritesForecast[favorite.id] = newFavoritesForecast
            updateViewState { copy(favoritesForecast = favoritesForecast.toPersistentMap()) }
        } else {
            execute(updateForecastWeatherUseCase, favorite, onException = ::provideException)
        }
    }

    private fun updateLocationsWithDebounce(debounce: Long, action: () -> Unit) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            updateViewState { loading() as HomeUiState }
            delay(debounce)
            action()
        }
    }

}