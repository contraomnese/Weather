package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.ClearIcon
import com.contraomnese.weather.core.ui.canvas.FogIcon
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition

@Composable
fun ConditionIcons(
    modifier: Modifier = Modifier,
    condition: WeatherCondition,
    isNight: Boolean = false,
    precipitationProbability: Int? = null,
) {
    when (condition) {
        WeatherCondition.CLEAR -> ClearIcon(modifier, isNight)

        WeatherCondition.PARTLY_CLOUDY -> PartlyCloudIcon(modifier, isNight)

        WeatherCondition.CLOUDY -> CloudyIcon(modifier)
        WeatherCondition.OVERCAST -> OverCastIcon(modifier)
        WeatherCondition.FOG -> FogIcon(modifier)
        WeatherCondition.FREEZING_FOG -> FogIcon(modifier, fogColor = Color(0xFF415B71))

        WeatherCondition.DRIZZLE_LIGHT, WeatherCondition.FREEZING_DRIZZLE_LIGHT,
            -> DrizzleIcon(modifier, intensity = Intensity.LIGHT)

        WeatherCondition.DRIZZLE_MODERATE,
            -> DrizzleIcon(modifier, intensity = Intensity.MODERATE)

        WeatherCondition.DRIZZLE_HEAVY, WeatherCondition.FREEZING_DRIZZLE_HEAVY,
            -> DrizzleIcon(modifier, intensity = Intensity.HEAVY)

        WeatherCondition.RAIN_SLIGHT -> RainIcon(modifier, intensity = Intensity.LIGHT, precipitationProbability)
        WeatherCondition.RAIN_MODERATE -> RainIcon(modifier, intensity = Intensity.MODERATE, precipitationProbability)
        WeatherCondition.RAIN_HEAVY -> RainIcon(modifier, intensity = Intensity.HEAVY, precipitationProbability)

        WeatherCondition.RAIN_SHOWERS_SLIGHT -> RainShowersIcon(
            modifier,
            intensity = Intensity.LIGHT,
            isNight,
            precipitationProbability
        )

        WeatherCondition.RAIN_SHOWERS_MODERATE -> RainShowersIcon(
            modifier,
            intensity = Intensity.MODERATE,
            isNight,
            precipitationProbability
        )

        WeatherCondition.RAIN_SHOWERS_HEAVY -> RainShowersIcon(
            modifier,
            intensity = Intensity.HEAVY,
            isNight,
            precipitationProbability
        )

        WeatherCondition.SNOW_SHOWERS_LIGHT, WeatherCondition.SNOW_SHOWERS_HEAVY,
        WeatherCondition.FREEZING_RAIN_LIGHT, WeatherCondition.FREEZING_RAIN_HEAVY,
            -> SleetCloudIcon(modifier)

        WeatherCondition.SNOW_FALL_SLIGHT -> SnowCloudIcon(
            modifier,
            intensity = Intensity.LIGHT,
            precipitationProbability
        )

        WeatherCondition.SNOW_FALL_MODERATE -> SnowCloudIcon(
            modifier,
            intensity = Intensity.MODERATE,
            precipitationProbability
        )

        WeatherCondition.SNOW_FALL_HEAVY -> SnowCloudIcon(
            modifier,
            intensity = Intensity.HEAVY,
            precipitationProbability
        )

        WeatherCondition.THUNDERSTORM -> ThunderCloudIcon(modifier)
        WeatherCondition.THUNDERSTORM_WITH_RAIN_LIGHT,
            -> ThunderCloudWithRainIcon(
            modifier,
            intensity = Intensity.MODERATE,
            precipitationProbability
        )

        WeatherCondition.THUNDERSTORM_WITH_RAIN_HEAVY,
            -> ThunderCloudWithRainIcon(
            modifier,
            intensity = Intensity.HEAVY,
            precipitationProbability
        )


        WeatherCondition.UNKNOWN -> Box(modifier)
    }
}


@Composable
@Preview
private fun ThunderIconPreview() {
    WeatherTheme {

        val sizeIcon = 64.dp

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.CLEAR,
                    isNight = false
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.CLEAR,
                    isNight = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.PARTLY_CLOUDY,
                    isNight = false
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.PARTLY_CLOUDY,
                    isNight = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.CLOUDY,
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.OVERCAST,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.DRIZZLE_LIGHT,
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.DRIZZLE_MODERATE,
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.DRIZZLE_HEAVY,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.RAIN_SHOWERS_SLIGHT,
                    precipitationProbability = 10
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.RAIN_SHOWERS_MODERATE,
                    precipitationProbability = 30
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.RAIN_SHOWERS_HEAVY,
                    isNight = true,
                    precipitationProbability = 60
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.RAIN_SLIGHT,
                    precipitationProbability = 10
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.RAIN_MODERATE,
                    precipitationProbability = 30
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.RAIN_HEAVY,
                    precipitationProbability = 60
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.SNOW_FALL_SLIGHT,
                    precipitationProbability = 10
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.SNOW_FALL_MODERATE,
                    precipitationProbability = 30
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.SNOW_FALL_HEAVY,
                    precipitationProbability = 60
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.THUNDERSTORM
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.THUNDERSTORM_WITH_RAIN_LIGHT,
                    precipitationProbability = 30
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.THUNDERSTORM_WITH_RAIN_HEAVY,
                    precipitationProbability = 60
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.FOG
                )
                ConditionIcons(
                    modifier = Modifier.size(sizeIcon),
                    condition = WeatherCondition.FREEZING_FOG,
                    precipitationProbability = 30
                )
            }
            ConditionIcons(
                modifier = Modifier.size(sizeIcon),
                condition = WeatherCondition.SNOW_SHOWERS_LIGHT,
            )
        }
    }
}