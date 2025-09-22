package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
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

@Immutable
internal data class WeatherUiState(
    override val isLoading: Boolean = false,
    val location: LocationInfoDomainModel,
    val weather: ForecastWeatherDomainModel? = null,
    val appSettings: AppSettings? = null,
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface WeatherEvent

internal class WeatherViewModel(
    useCaseExecutorProvider: UseCaseExecutorProvider,
    notificationMonitor: NotificationMonitor,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val locationId: Int,
    private val lat: Double,
    private val lot: Double,
) : BaseViewModel<WeatherUiState, WeatherEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(getAppSettingsUseCase, ::updateAppSettings)
        updateCurrentLocation(
            LocationInfoDomainModel(
                id = locationId,
                name = "",
                countryName = "",
                point = CoordinatesDomainModel(
                    latitude = LatitudeDomainModel(lat),
                    longitude = LongitudeDomainModel(lot)
                )
            )
        )
    }

    override fun initialState(): WeatherUiState =
        WeatherUiState(isLoading = true, location = LocationInfoDomainModel.EMPTY)

    override fun onEvent(event: WeatherEvent) = Unit

    private fun updateCurrentLocation(newLocation: LocationInfoDomainModel) {
        updateViewState { copy(location = newLocation) }
        observe(observeForecastWeatherUseCase, newLocation, ::updateCurrentWeather, ::provideException)
    }

    private fun updateCurrentWeather(newWeather: ForecastWeatherDomainModel?) {
        if (newWeather == null) {
            execute(updateForecastWeatherUseCase, uiState.value.location, onException = ::provideException)
        } else updateViewState { copy(weather = newWeather, isLoading = false) }
    }

    private fun updateAppSettings(newAppSettings: AppSettings) {
        updateViewState { copy(appSettings = newAppSettings) }
    }

}