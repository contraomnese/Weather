package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemThickness1
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay


@Composable
fun ForecastDailyColumn(
    modifier: Modifier = Modifier,
    items: List<ForecastDay>,
    currentTemperature: Int,
    temperatureUnit: TemperatureUnit,
) {

    val maxRangeTemperature = items.maxOf { it.maxTemperature }
    val minRangeTemperature = items.minOf { it.minTemperature }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(padding4)
    ) {
        items.forEachIndexed { index, it ->
            ForecastDailyItem(
                dayName = it.dayName,
                dayNumber = it.dayNumber,
                conditionCode = it.conditionCode,
                minTemperature = it.minTemperature,
                maxTemperature = it.maxTemperature,
                maxRangeTemperature = maxRangeTemperature,
                minRangeTemperature = minRangeTemperature,
                currentTemperature = if (index == 0) currentTemperature else null,
                temperatureUnit = temperatureUnit
            )
            if (index != items.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    thickness = itemThickness1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ForecastHourlyLazyRowPreview() {
    WeatherTheme {

        val codes = (1000..1200 step 3).toList()
        val minTemperature = (0..10)
        val maxTemperature = (16..25)

        ForecastDailyColumn(
            items = List(10) { index ->
                ForecastDay(
                    dayNumber = "33.03",
                    dayName = "Mon",
                    conditionCode = codes.random(),
                    maxTemperature = maxTemperature.random(),
                    minTemperature = minTemperature.random(),
                    totalRainFull = 123,
                )
            },
            currentTemperature = 14,
            temperatureUnit = TemperatureUnit.Celsius
        )
    }
}