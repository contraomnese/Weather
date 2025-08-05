package com.contraomnese.weather.home.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.home.model.CityDomainModel
import com.contraomnese.weather.domain.home.model.CityPresentation
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList


@Immutable
internal data class HomeUiState(
    override val isLoading: Boolean = false,
    val city: CityPresentation = CityPresentation(""),
    val cities: ImmutableList<CityDomainModel> = persistentListOf(),
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface HomeEvent {
    data class CityChanged(val newCity: String) : HomeEvent
}

internal class HomeViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getLocationsUseCase: GetLocationsUseCase,
) : BaseViewModel<HomeUiState, HomeEvent>(useCaseExecutorProvider, notificationMonitor) {

    override fun initialState(): HomeUiState = HomeUiState()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CityChanged -> onCityChanged(event.newCity)
        }
    }

    private fun onCityChanged(newCity: String) {
        updateViewState { copy(city = CityPresentation(newCity)) }
        execute(getLocationsUseCase, newCity, ::onCitiesUpdate, ::provideException)
    }

    private fun onCitiesUpdate(newCities: List<CityDomainModel>) {
        updateViewState { copy(cities = newCities.toPersistentList()) }
    }

}