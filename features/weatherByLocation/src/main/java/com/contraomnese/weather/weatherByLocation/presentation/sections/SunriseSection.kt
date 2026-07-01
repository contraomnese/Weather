package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.contraomnese.weather.domain.weatherByLocation.model.LocationTime
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime

data class SunriseSection(
    override val bodyHeight: MutableState<Float>? = null,
    override val bodyMaxHeight: Float? = bodyHeight?.value,
    override val icon: ImageVector = WeatherIcons.Sunrise,
    override val title: Int = R.string.day_title,
) : WeatherSection {

    override fun initBodyHeight(newHeight: Float) =
        copy(
            bodyHeight = mutableFloatStateOf(newHeight),
            bodyMaxHeight = bodyMaxHeight ?: newHeight
        )

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


        var localTime by remember {
            mutableStateOf(
                LocationTime.fromInstant(Clock.System.now(), location.timeZone)
            )
        }

        val isDay by remember {
            derivedStateOf {
                localTime.time >= sunrise.time && localTime.time <= sunset.time
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                val instant = Clock.System.now()
                val dateTime = instant.toLocalDateTime(location.timeZone)
                localTime = LocationTime.fromInstant(instant, location.timeZone)
                val millisToNextMinute = 60_000L - dateTime.second * 1000
                delay(millisToNextMinute)
            }
        }

        CollapsableContainer(
            headerHeight = headerSectionHeight,
            headerTitle = stringResource(if (isDay) R.string.day_title else R.string.night_title),
            headerIcon = icon,
            currentBodyHeight = bodyHeight?.value,
            onContentMeasured = measureContainerHeight
        ) {
            SunriseItem(
                modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
                sunriseTime = sunrise,
                sunsetTime = sunset,
                localTime = localTime,
                isDay = isDay
            )
        }
    }
}