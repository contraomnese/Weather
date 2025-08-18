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
    val hourlyForecastStub: List<String> = listOf(
        "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"
    ),
    val dailyForecastStub: List<Pair<String, String>> = listOf(
        "Сегодня" to "+25° / +17°",
        "Завтра" to "+26° / +18°",
        "Пн" to "+27° / +19°",
        "Вт" to "+28° / +20°",
        "Ср" to "+29° / +21°",
        "Чт" to "+30° / +22°",
        "Пт" to "+31° / +23°",
        "Сб" to "+32° / +24°",
        "Вс" to "+33° / +25°",
        "Пн" to "+34° / +26°"
    ),
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