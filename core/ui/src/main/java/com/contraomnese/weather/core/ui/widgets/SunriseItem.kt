package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.SunriseSunset
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.weatherByLocation.model.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.LocationTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun SunriseItem(
    modifier: Modifier = Modifier,
    sunriseTime: LocationTime,
    sunsetTime: LocationTime,
    currentTime: LocationDateTime,
    isMidDay: Boolean,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(padding16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(padding8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentTime.toLocalTime(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isMidDay) stringResource(
                    R.string.sunrise_time_title,
                    sunriseTime.toLocalTime()
                ) else stringResource(R.string.sunset_time_title, sunsetTime.toLocalTime()),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
        SunriseSunset(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clipToBounds(),
            sunriseMinutes = sunriseTime.toMinutes(),
            sunsetMinutes = sunsetTime.toMinutes(),
            currentMinutes = currentTime.toMinutes()
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
            currentTime = LocationDateTime(LocalDateTime(1, 1, 1, 9, 23)),
            isMidDay = true
        )
    }
}