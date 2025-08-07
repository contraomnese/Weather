package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.weatherByLocation.model.CurrentWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.GeoLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetCurrentWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetGeoLocationUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider

@Immutable
internal data class WeatherUiState(
    override val isLoading: Boolean = false,
    val location: GeoLocationDomainModel,
    val weather: CurrentWeatherDomainModel? = null,
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface WeatherEvent {
    data class LocationChanged(val newLocation: String) : WeatherEvent
}

internal class WeatherViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getGeoLocationUseCase: GetGeoLocationUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val locationId: Int,
) : BaseViewModel<WeatherUiState, WeatherEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        execute(getGeoLocationUseCase, locationId, ::updateCurrentLocation, ::provideException)
    }

    override fun initialState(): WeatherUiState =
        WeatherUiState(isLoading = true, location = GeoLocationDomainModel.EMPTY)

    override fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.LocationChanged -> Unit
        }
    }

    private fun updateCurrentLocation(newLocation: GeoLocationDomainModel) {
        updateViewState { copy(location = newLocation) }
        execute(getCurrentWeatherUseCase, newLocation.getPoint(), ::updateCurrentWeather, ::provideException)
    }

    private fun updateCurrentWeather(newWeather: CurrentWeatherDomainModel) {
        updateViewState { copy(weather = newWeather, isLoading = false) }
    }

}