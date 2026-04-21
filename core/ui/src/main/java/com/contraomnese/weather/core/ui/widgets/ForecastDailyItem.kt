package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.TemperatureRangeLine
import com.contraomnese.weather.core.ui.icons.ConditionIcons
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight10
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.itemWidth32
import com.contraomnese.weather.design.theme.itemWidth40
import com.contraomnese.weather.design.theme.itemWidth64
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

@Composable
fun ForecastDailyItem(
    modifier: Modifier = Modifier,
    dayNumber: String,
    dayName: String,
    condition: WeatherCondition,
    minTemperature: Int,
    maxTemperature: Int,
    maxRangeTemperature: Int,
    minRangeTemperature: Int,
    currentTemperature: Int? = null,
    temperatureUnit: TemperatureUnit,
    precipitationProbability: Int? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight64),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding8),
    ) {

        Text(
            modifier = Modifier.width(itemWidth64),
            text = dayNumber,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            modifier = Modifier.width(itemWidth32),
            text = dayName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        ConditionIcons(
            condition = condition,
            precipitationProbability = precipitationProbability,
            modifier = Modifier.size(itemWidth40)
        )
        Text(
            modifier = Modifier.width(itemWidth40),
            text = stringResource(R.string.current_temperature_title, minTemperature.toString()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            textAlign = TextAlign.Right
        )
        TemperatureRangeLine(
            minRange = minRangeTemperature,
            maxRange = maxRangeTemperature,
            min = minTemperature,
            current = currentTemperature,
            max = maxTemperature,
            temperatureUnit = temperatureUnit,
            modifier = Modifier
                .weight(1f)
                .height(itemHeight10),
        )
        Text(
            modifier = Modifier.width(itemWidth40),
            text = stringResource(R.string.current_temperature_title, maxTemperature.toString()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5", backgroundColor = 0xFF1C232B, showSystemUi = false)
@Composable
private fun ForecastDailyItemPreview() {
    WeatherTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ForecastDailyItem(
                dayNumber = "21.06",
                dayName = "Пн",
                condition = WeatherCondition.RAIN_SLIGHT,
                minTemperature = 7,
                maxTemperature = 14,
                maxRangeTemperature = 16,
                minRangeTemperature = 7,
                currentTemperature = 11,
                temperatureUnit = TemperatureUnit.Celsius,
                precipitationProbability = 10
            )
            ForecastDailyItem(
                dayNumber = "22.06",
                dayName = "Чт",
                condition = WeatherCondition.OVERCAST,
                minTemperature = 8,
                maxTemperature = 15,
                maxRangeTemperature = 16,
                minRangeTemperature = 7,
                temperatureUnit = TemperatureUnit.Celsius
            )
            ForecastDailyItem(
                dayNumber = "31.06",
                dayName = "Сб",
                condition = WeatherCondition.CLEAR,
                minTemperature = 11,
                maxTemperature = 16,
                maxRangeTemperature = 16,
                minRangeTemperature = 7,
                temperatureUnit = TemperatureUnit.Celsius
            )
        }

    }
}