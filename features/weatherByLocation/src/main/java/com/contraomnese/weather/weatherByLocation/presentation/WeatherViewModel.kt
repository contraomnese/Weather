package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetDetailsLocationUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider

@Immutable
internal data class WeatherUiState(
    override val isLoading: Boolean = false,
    val location: DetailsLocationDomainModel,
    val weather: ForecastWeatherDomainModel? = null,
    val appSettings: AppSettings? = null,
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface WeatherEvent

internal class WeatherViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
    private val getGeoLocationUseCase: GetDetailsLocationUseCase,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val locationId: Int,
) : BaseViewModel<WeatherUiState, WeatherEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(getAppSettingsUseCase, ::updateAppSettings)
        execute(getGeoLocationUseCase, locationId, ::updateCurrentLocation, ::provideException)
    }

    override fun initialState(): WeatherUiState =
        WeatherUiState(isLoading = true, location = DetailsLocationDomainModel.EMPTY)

    override fun onEvent(event: WeatherEvent) = Unit

    private fun updateCurrentLocation(newLocation: DetailsLocationDomainModel) {
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