package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.usecase.GetAppSettingsUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveForecastWeatherUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastWeatherUseCase
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
internal data class WeatherUiState(
    override val isLoading: Boolean = false,
    val locationId: Int? = null,
    val weather: ForecastWeatherDomainModel? = null,
    val appSettings: AppSettings? = null,
    val favorites: ImmutableList<LocationInfoDomainModel> = persistentListOf(),
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface WeatherEvent {
    data class LocationChanged(val locationId: Int) : WeatherEvent
}

internal class WeatherViewModel(
    useCaseExecutorProvider: UseCaseExecutorProvider,
    notificationMonitor: NotificationMonitor,
    private val observeForecastWeatherUseCase: ObserveForecastWeatherUseCase,
    private val updateForecastWeatherUseCase: UpdateForecastWeatherUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val navLocationId: Int,
) : BaseViewModel<WeatherUiState, WeatherEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(getAppSettingsUseCase, ::updateAppSettings)
        observe(observeFavoritesUseCase, ::onFavoritesUpdate, ::provideException)
        updateCurrentLocation(navLocationId)
    }

    override fun initialState(): WeatherUiState {
        return WeatherUiState(isLoading = true, locationId = navLocationId)
    }

    override fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.LocationChanged -> updateCurrentLocation(uiState.value.favorites[event.locationId].id)
        }
    }

    private fun updateAppSettings(newAppSettings: AppSettings) {
        updateViewState { copy(appSettings = newAppSettings) }
    }

    private fun onFavoritesUpdate(newFavorites: List<LocationInfoDomainModel>) {
        updateViewState { copy(favorites = newFavorites.toPersistentList()) }
    }

    private fun updateCurrentLocation(newLocationId: Int) {
        updateViewState { copy(locationId = newLocationId) }
        observe(observeForecastWeatherUseCase, newLocationId, ::updateCurrentWeather, ::provideException)
    }

    private fun updateCurrentWeather(newWeather: ForecastWeatherDomainModel?) {
        newWeather?.let {
            updateViewState { copy(weather = newWeather, isLoading = false) }
        } ?: execute(updateForecastWeatherUseCase, uiState.value.locationId!!, onException = ::provideException)
    }
}