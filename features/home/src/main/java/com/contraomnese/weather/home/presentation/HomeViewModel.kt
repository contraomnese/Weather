package com.contraomnese.weather.home.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.home.model.LocationPresentation
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Immutable
internal data class HomeUiState(
    override val isLoading: Boolean = false,
    val location: LocationPresentation = LocationPresentation(""),
    val locations: ImmutableList<LocationDomainModel> = persistentListOf(),
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface HomeEvent {
    data class LocationChanged(val newLocation: String) : HomeEvent
}

internal class HomeViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getLocationsUseCase: GetLocationsUseCase,
) : BaseViewModel<HomeUiState, HomeEvent>(useCaseExecutorProvider, notificationMonitor) {

    private var searchJob: Job? = null

    override fun initialState(): HomeUiState = HomeUiState()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LocationChanged -> onLocationChanged(event.newLocation)
        }
    }

    private fun onLocationChanged(newCity: String) {
        updateViewState { copy(location = LocationPresentation(newCity)) }

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

    private fun onCitiesUpdate(newCities: List<LocationDomainModel>) {
        updateViewState { copy(locations = newCities.toPersistentList(), isLoading = false) }
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