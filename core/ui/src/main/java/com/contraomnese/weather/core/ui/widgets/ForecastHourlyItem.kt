package com.contraomnese.weather.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.icons.ConditionIcons
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.itemHeight64
import com.contraomnese.weather.design.theme.itemWidth48
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

@Composable
fun ForecastHourlyItem(
    modifier: Modifier = Modifier,
    time: String,
    condition: WeatherCondition,
    temperature: String,
    isNow: Boolean = false,
    isDay: Boolean = false,
    precipitationProbability: Int,
) {
    Column(
        modifier = modifier
            .width(itemWidth48)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = time.take(2),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        ConditionIcons(
            condition = condition,
            isNight = !isDay,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight64),
            precipitationProbability = precipitationProbability
        )
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(R.string.current_temperature_title, temperature),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun ForecastHourlyItemPreview() {
    WeatherTheme {
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        ) {
            ForecastHourlyItem(
                time = "02:00",
                condition = WeatherCondition.DRIZZLE_LIGHT,
                temperature = "19",
                precipitationProbability = 10
            )
        }

    }
}

@Preview
@Composable
private fun ForecastHourlyItemNowPreview() {
    WeatherTheme {
        Box(
            modifier = Modifier
                .height(itemHeight140)
                .fillMaxWidth()
        ) {
            ForecastHourlyItem(
                time = "03:00",
                condition = WeatherCondition.RAIN_HEAVY,
                temperature = "191",
                isNow = true,
                precipitationProbability = 10
            )
        }
    }
}