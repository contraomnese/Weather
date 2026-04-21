package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.itemThickness1
import com.contraomnese.weather.design.theme.padding4
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastDay
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition


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
                Modifier
                    .height(itemHeight64)
                    .fillMaxWidth(),
                dayName = it.dayName,
                dayNumber = it.dayNumber,
                condition = it.condition,
                minTemperature = it.minTemperature,
                maxTemperature = it.maxTemperature,
                maxRangeTemperature = maxRangeTemperature,
                minRangeTemperature = minRangeTemperature,
                currentTemperature = if (index == 0) currentTemperature else null,
                temperatureUnit = temperatureUnit,
                precipitationProbability = it.precipitationProbability
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

@Preview(locale = "ru")
@Composable
private fun ForecastHourlyLazyRowPreview() {
    WeatherTheme {

        val minTemperature = (0..15)
        val maxTemperature = (4..8)

        ForecastDailyColumn(
            items = List(10) {
                ForecastDay(
                    dayNumber = "9.04",
                    dayName = listOf("Чт", "Вт", "Ср", "Пт").random(),
                    condition = WeatherCondition.RAIN_MODERATE,
                    maxTemperature = maxTemperature.random(),
                    minTemperature = minTemperature.random(),
                    totalRainFull = 123,
                    precipitationProbability = 10
                )
            },
            currentTemperature = 14,
            temperatureUnit = TemperatureUnit.Celsius
        )
    }
}