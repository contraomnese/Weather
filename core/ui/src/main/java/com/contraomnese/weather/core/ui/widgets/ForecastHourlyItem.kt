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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.icons.ConditionIcons
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight140
import com.contraomnese.weather.design.theme.itemWidth48
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

@Composable
fun ForecastHourlyItem(
    modifier: Modifier = Modifier,
    time: String,
    condition: WeatherCondition,
    temperature: String,
    isSunCycle: Boolean = false,
    isDay: Boolean = false,
    precipitationProbability: Int,
) {
    val regularWidthModifier = Modifier.width(itemWidth48)
    val sunCycleWidthModifier = if (isSunCycle) Modifier.width(80.dp) else regularWidthModifier

    Column(
        modifier = modifier
            .then(sunCycleWidthModifier)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = if (isSunCycle) time else time.take(2),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        ConditionIcons(
            condition = condition,
            isNight = !isDay,
            modifier = regularWidthModifier,
            precipitationProbability = precipitationProbability
        )
        Text(
            modifier = Modifier.wrapContentSize(),
            text =
                if (isSunCycle && condition == WeatherCondition.SUNRISE) stringResource(R.string.sunrise_title)
                else if (isSunCycle && condition == WeatherCondition.SUNSET) stringResource(R.string.sunset_title)
                else stringResource(R.string.current_temperature_title, temperature),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
                isSunCycle = false,
                precipitationProbability = 10
            )
        }
    }
}

@Preview
@Composable
private fun ForecastHourlyItemSunrisePreview() {
    WeatherTheme {
        Box(
            modifier = Modifier
                .height(itemHeight140)
                .fillMaxWidth()
        ) {
            ForecastHourlyItem(
                time = "03:13",
                condition = WeatherCondition.SUNRISE,
                temperature = "",
                isDay = true,
                isSunCycle = true,
                precipitationProbability = 0
            )
        }
    }
}