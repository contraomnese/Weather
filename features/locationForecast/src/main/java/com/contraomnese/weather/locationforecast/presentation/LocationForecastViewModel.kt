package com.contraomnese.weather.locationforecast.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.locationForecast.model.CurrentWeatherDomainModel
import com.contraomnese.weather.domain.locationForecast.model.LocationForecastDomainModel
import com.contraomnese.weather.domain.locationForecast.usecase.GetCurrentWeatherUseCase
import com.contraomnese.weather.domain.locationForecast.usecase.GetLocationForecastUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider

@Immutable
internal data class LocationForecastUiState(
    override val isLoading: Boolean = false,
    val location: LocationForecastDomainModel,
    val weather: CurrentWeatherDomainModel? = null,
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface LocationForecastEvent {
    data class LocationChanged(val newLocation: String) : LocationForecastEvent
}

internal class LocationForecastViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getLocationForecastUseCase: GetLocationForecastUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val locationId: Int,
) : BaseViewModel<LocationForecastUiState, LocationForecastEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        execute(getLocationForecastUseCase, locationId, ::updateCurrentLocation, ::provideException)
    }

    override fun initialState(): LocationForecastUiState =
        LocationForecastUiState(isLoading = true, location = LocationForecastDomainModel.EMPTY)

    override fun onEvent(event: LocationForecastEvent) {
        when (event) {
            is LocationForecastEvent.LocationChanged -> Unit
        }
    }

    private fun updateCurrentLocation(newLocation: LocationForecastDomainModel) {
        updateViewState { copy(location = newLocation) }
        execute(getCurrentWeatherUseCase, newLocation.getPoint(), ::updateCurrentWeather, ::provideException)
    }

    private fun updateCurrentWeather(newWeather: CurrentWeatherDomainModel) {
        updateViewState { copy(weather = newWeather, isLoading = false) }
    }

}