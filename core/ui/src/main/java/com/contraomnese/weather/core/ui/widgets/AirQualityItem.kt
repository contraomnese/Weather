package com.contraomnese.weather.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.contraomnese.weather.core.ui.canvas.AqiIndexRangeLine
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.itemHeight6
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.weatherByLocation.model.PollutantLevel


@Composable
fun AirQualityItem(
    modifier: Modifier = Modifier,
    aqiIndex: Int,
    coLevel: PollutantLevel,
    no2Level: PollutantLevel,
    o3Level: PollutantLevel,
    so2Level: PollutantLevel,
    pm25Level: PollutantLevel,
    pm10Level: PollutantLevel,
) {

    val aqi = remember(aqiIndex) {
        when (aqiIndex) {
        in 1..3 -> GoodAqi()
        in 4..6 -> ModerateAqi()
        in 7..9 -> BadAqi()
        else -> VeryBadAqi()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(padding8)
    ) {
        Text(
            text = stringResource(aqi.text, aqiIndex),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(aqi.description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        AqiIndexRangeLine(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight6),
            current = aqiIndex.toFloat()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CO",
                style = MaterialTheme.typography.bodyMedium,
                color = coLevel.toColor()
            )
            Text(
                text = "NO2",
                style = MaterialTheme.typography.bodyMedium,
                color = no2Level.toColor()
            )
            Text(
                text = "O3",
                style = MaterialTheme.typography.bodyMedium,
                color = o3Level.toColor()
            )
            Text(
                text = "SO2",
                style = MaterialTheme.typography.bodyMedium,
                color = so2Level.toColor()
            )
            Text(
                text = "PM2.5",
                style = MaterialTheme.typography.bodyMedium,
                color = pm25Level.toColor()
            )
            Text(
                text = "PM10",
                style = MaterialTheme.typography.bodyMedium,
                color = pm10Level.toColor()
            )
        }
    }
}

private val GoodColor = Color(0xFFFFFFFF)
private val ModerateColor = Color(0xFFDCD807)
private val BadColor = Color(0xFFAA5111)

private fun PollutantLevel.toColor(): Color = when (this) {
    PollutantLevel.Good -> GoodColor
    PollutantLevel.Moderate -> ModerateColor
    PollutantLevel.Bad -> BadColor
}

private interface Aqi {
    @get:StringRes
    val text: Int

    @get:StringRes
    val description: Int
}

private data class GoodAqi(
    override val text: Int = R.string.aqi_good,
    override val description: Int = R.string.aqi_good_desc,
) : Aqi

private data class ModerateAqi(
    override val text: Int = R.string.aqi_moderate,
    override val description: Int = R.string.aqi_moderate_desc,
) : Aqi

private data class BadAqi(
    override val text: Int = R.string.aqi_bad,
    override val description: Int = R.string.aqi_bad_desc,
) : Aqi

private data class VeryBadAqi(
    override val text: Int = R.string.aqi_very_bad,
    override val description: Int = R.string.aqi_very_bad_desc,
) : Aqi

@Preview
@Composable
private fun AirQualityItemPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        AirQualityItem(
            modifier = modifier,
            aqiIndex = 10,
            coLevel = PollutantLevel.Good,
            no2Level = PollutantLevel.Good,
            o3Level = PollutantLevel.Moderate,
            so2Level = PollutantLevel.Good,
            pm10Level = PollutantLevel.Bad,
            pm25Level = PollutantLevel.Good
        )
    }
}