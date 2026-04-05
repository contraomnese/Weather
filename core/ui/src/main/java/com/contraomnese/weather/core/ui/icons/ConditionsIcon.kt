package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.CloudyIcon
import com.contraomnese.weather.core.ui.canvas.DrizzleIcon
import com.contraomnese.weather.core.ui.canvas.FogIcon
import com.contraomnese.weather.core.ui.canvas.Intensity
import com.contraomnese.weather.core.ui.canvas.MoonIcon
import com.contraomnese.weather.core.ui.canvas.OverCastIcon
import com.contraomnese.weather.core.ui.canvas.PartlyCloudIcon
import com.contraomnese.weather.core.ui.canvas.RainCloudIcon
import com.contraomnese.weather.core.ui.canvas.RainIcon
import com.contraomnese.weather.core.ui.canvas.SleetCloudIcon
import com.contraomnese.weather.core.ui.canvas.SnowCloudIcon
import com.contraomnese.weather.core.ui.canvas.SnowFlakeIcon
import com.contraomnese.weather.core.ui.canvas.SunIcon
import com.contraomnese.weather.core.ui.canvas.ThunderCloudIcon
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

@Composable
fun ConditionsIcon(condition: WeatherCondition, modifier: Modifier = Modifier, isNight: Boolean = false) {
    when (condition) {
        WeatherCondition.CLEAR -> if (isNight) MoonIcon(modifier) else SunIcon(modifier)

        WeatherCondition.PARTLY_CLOUDY -> PartlyCloudIcon(modifier)

        WeatherCondition.CLOUDY -> CloudyIcon(modifier)
        WeatherCondition.OVERCAST -> OverCastIcon(modifier)
        WeatherCondition.FOG, WeatherCondition.FREEZING_FOG -> FogIcon(modifier)

        WeatherCondition.DRIZZLE_LIGHT, WeatherCondition.FREEZING_DRIZZLE_LIGHT,
            -> DrizzleIcon(modifier, intensity = Intensity.LIGHT)

        WeatherCondition.DRIZZLE_MODERATE -> DrizzleIcon(modifier, intensity = Intensity.MODERATE)
        WeatherCondition.DRIZZLE_HEAVY, WeatherCondition.FREEZING_DRIZZLE_HEAVY,
            -> DrizzleIcon(modifier, intensity = Intensity.HEAVY)

        WeatherCondition.RAIN_SLIGHT -> RainIcon(modifier, intensity = Intensity.LIGHT)
        WeatherCondition.RAIN_MODERATE -> RainIcon(modifier, intensity = Intensity.MODERATE)
        WeatherCondition.RAIN_HEAVY -> RainIcon(modifier, intensity = Intensity.HEAVY)

        WeatherCondition.SNOW_SHOWERS_LIGHT, WeatherCondition.SNOW_SHOWERS_HEAVY,
        WeatherCondition.FREEZING_RAIN_LIGHT, WeatherCondition.FREEZING_RAIN_HEAVY,
            -> SleetCloudIcon()

        WeatherCondition.SNOW_FALL_SLIGHT, WeatherCondition.SNOW_FALL_MODERATE, WeatherCondition.SNOW_FALL_HEAVY,
            -> SnowFlakeIcon()

        WeatherCondition.RAIN_SHOWERS_SLIGHT, WeatherCondition.RAIN_SHOWERS_MODERATE, WeatherCondition.RAIN_SHOWERS_HEAVY,
            -> RainCloudIcon(modifier)

        WeatherCondition.THUNDERSTORM, WeatherCondition.THUNDERSTORM_WITH_RAIN_LIGHT,
        WeatherCondition.THUNDERSTORM_WITH_RAIN_HEAVY,
            -> ThunderCloudIcon(modifier)

        WeatherCondition.UNKNOWN -> Box(modifier)
    }
}


@Composable
@Preview
private fun ThunderIconPreview() {
    WeatherTheme {

        val sizeIcon = 64.dp

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SunIcon(modifier = Modifier.size(sizeIcon))
                MoonIcon(modifier = Modifier.size(sizeIcon))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PartlyCloudIcon(modifier = Modifier.size(sizeIcon))
                PartlyCloudIcon(modifier = Modifier.size(sizeIcon), isNight = true)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CloudyIcon(modifier = Modifier.size(sizeIcon))
                OverCastIcon(modifier = Modifier.size(sizeIcon))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DrizzleIcon(modifier = Modifier.size(sizeIcon), intensity = Intensity.LIGHT)
                DrizzleIcon(modifier = Modifier.size(sizeIcon), intensity = Intensity.MODERATE)
                DrizzleIcon(modifier = Modifier.size(sizeIcon), intensity = Intensity.HEAVY)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RainIcon(Modifier.size(sizeIcon), intensity = Intensity.LIGHT)
                RainIcon(Modifier.size(sizeIcon), intensity = Intensity.MODERATE)
                RainIcon(Modifier.size(sizeIcon), intensity = Intensity.HEAVY)
            }
            ThunderCloudIcon(modifier = Modifier.size(sizeIcon))
            SnowCloudIcon(modifier = Modifier.size(sizeIcon))
            FogIcon(modifier = Modifier.size(sizeIcon))
            SleetCloudIcon(modifier = Modifier.size(sizeIcon))
        }
    }
}