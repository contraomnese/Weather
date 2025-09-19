package com.contraomnese.weather.weatherByLocation.presentation.data

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons


sealed interface WeatherSection {

    val bodyHeight: Float?
    val bodyMaxHeight: Float?
    val icon: ImageVector

    @get:StringRes
    val title: Int

    fun copyWithBodyHeight(newHeight: Float): WeatherSection

    operator fun component1(): Float? = bodyHeight
    operator fun component2(): Float? = bodyMaxHeight
}

data class HourlyForecastSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Today,
    override val title: Int = R.string.today_forecast_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class DailyForecastSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Daily,
    override val title: Int = R.string.daily_forecast_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class AqiSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Aqi,
    override val title: Int = R.string.aqi_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class SunriseSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Sunrise,
    val isSunUp: Boolean,
    override val title: Int = when (isSunUp) {
        true -> R.string.day_title
        false -> R.string.night_title
    },
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class UVIndexSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.UvIndex,
    override val title: Int = R.string.uv_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class HumiditySection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Humidity,
    override val title: Int = R.string.humidity_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class RainfallSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Rainfall,
    override val title: Int = R.string.rainfall_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class WindSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Wind,
    override val title: Int = R.string.wind_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

data class PressureSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Pressure,
    override val title: Int = R.string.pressure_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)
}

