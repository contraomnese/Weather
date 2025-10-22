package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.SunriseSunset
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.itemHeight160
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.weatherByLocation.model.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.LocationTime
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun SunriseItem(
    modifier: Modifier = Modifier,
    sunriseTime: LocationTime,
    sunsetTime: LocationTime,
    timeZone: TimeZone,
    isDay: Boolean,
) {

    var localTime by remember {
        mutableStateOf(
            LocationDateTime(Clock.System.now().toLocalDateTime(timeZone))
        )
    }

    LaunchedEffect(timeZone) {
        while (true) {
            val instant = Clock.System.now().toLocalDateTime(timeZone)
            localTime = LocationDateTime(instant)
            val millisToNextMinute = 60_000L - instant.second * 1000
            delay(millisToNextMinute)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight160),
        horizontalArrangement = Arrangement.spacedBy(padding16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(padding8),
        ) {

            Text(
                text = stringResource(R.string.local_time_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = localTime.toLocalTime(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isDay) stringResource(
                    R.string.sunset_time_title, sunsetTime.toLocalTime()
                ) else stringResource(R.string.sunrise_time_title, sunriseTime.toLocalTime()),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
        SunriseSunset(
            modifier = Modifier
                .weight(2f)
                .height(itemHeight140)
                .padding(horizontal = padding8)
                .clipToBounds(),
            sunriseMinutes = sunriseTime.toMinutes(),
            sunsetMinutes = sunsetTime.toMinutes(),
            currentMinutes = localTime.toMinutes()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x79397E93)
@Composable
fun SunriseItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {

        SunriseItem(
            modifier = modifier,
            sunriseTime = LocationTime(LocalTime(7, 23, 0, 0)),
            sunsetTime = LocationTime(LocalTime(18, 15, 0, 0)),
            timeZone = TimeZone.of("Europe/Moscow"),
            isDay = true
        )
    }
}