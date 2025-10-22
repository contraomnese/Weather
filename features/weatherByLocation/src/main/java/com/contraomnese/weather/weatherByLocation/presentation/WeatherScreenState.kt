package com.contraomnese.weather.weatherByLocation.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.presentation.architecture.MviState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
internal data class WeatherScreenState(
    override val isLoading: Boolean = false,
    val locationId: Int,
    val weather: Forecast? = null,
    val appSettings: AppSettings? = null,
    val favorites: ImmutableList<Location> = persistentListOf(),
) : MviState {

    fun setWeather(weather: Forecast): WeatherScreenState = copy(weather = weather)
    fun setAppSettings(appSettings: AppSettings): WeatherScreenState = copy(appSettings = appSettings)
    fun setFavorites(favorites: List<Location>): WeatherScreenState = copy(favorites = favorites.toPersistentList())
    fun setLocationId(id: Int): WeatherScreenState = copy(locationId = id)

    fun init(weather: Forecast, appSettings: AppSettings, favorites: List<Location>) = copy(
        isLoading = false,
        weather = weather,
        appSettings = appSettings,
        favorites = favorites.toPersistentList()
    )

    companion object {
        fun default(id: Int) = WeatherScreenState(isLoading = true, locationId = id)
    }
}