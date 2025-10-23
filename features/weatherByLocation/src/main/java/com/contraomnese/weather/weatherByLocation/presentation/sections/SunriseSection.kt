package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.SunriseItem
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

data class SunriseSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Sunrise,
    val isDay: Boolean,
    override val title: Int = when (isDay) {
        true -> R.string.day_title
        false -> R.string.night_title
    },
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)

    @Composable
    override fun Render(
        headerSectionHeight: Dp,
        weather: Forecast,
        appSettings: AppSettings,
        measureContainerHeight: (Int) -> Unit,
        progress: Float,
    ) {
        val today = weather.forecast.today
        val location = weather.location

        val sunrise = today.sunrise
        val sunset = today.sunset
        val isDay = weather.today.isDay

        if (sunrise != null && sunset != null) {
            CollapsableContainer(
                headerHeight = headerSectionHeight,
                headerTitle = stringResource(title),
                headerIcon = icon,
                currentBodyHeight = bodyHeight,
                onContentMeasured = measureContainerHeight
            ) {
                SunriseItem(
                    modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
                    sunriseTime = sunrise,
                    sunsetTime = sunset,
                    timeZone = location.timeZone,
                    isDay = isDay
                )
            }
        }
    }
}