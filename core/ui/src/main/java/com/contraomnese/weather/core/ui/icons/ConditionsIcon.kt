package com.contraomnese.weather.core.ui.icons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.core.ui.canvas.FogIcon
import com.contraomnese.weather.core.ui.canvas.MoonIcon
import com.contraomnese.weather.core.ui.canvas.PartlyCloudIcon
import com.contraomnese.weather.core.ui.canvas.RainCloudIcon
import com.contraomnese.weather.core.ui.canvas.SleetCloudIcon
import com.contraomnese.weather.core.ui.canvas.SnowCloudIcon
import com.contraomnese.weather.core.ui.canvas.SunIcon
import com.contraomnese.weather.core.ui.canvas.ThunderCloudIcon
import com.contraomnese.weather.core.ui.canvas.TwoCloudIcon
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.domain.weatherByLocation.model.internal.CompactWeatherCondition

@Composable
fun ConditionsIcon(condition: CompactWeatherCondition, modifier: Modifier = Modifier, isNight: Boolean = false) {
    when (condition) {
        CompactWeatherCondition.CLEAR -> if (isNight) MoonIcon(modifier) else SunIcon(modifier)
        CompactWeatherCondition.PARTLY_CLOUDY -> PartlyCloudIcon(modifier)
        CompactWeatherCondition.CLOUDY -> TwoCloudIcon(modifier)
        CompactWeatherCondition.FOG -> FogIcon(modifier)
        CompactWeatherCondition.RAIN -> RainCloudIcon(modifier)
        CompactWeatherCondition.SNOW -> SnowCloudIcon(modifier)
        CompactWeatherCondition.THUNDER -> ThunderCloudIcon(modifier)
        CompactWeatherCondition.SLEET -> SleetCloudIcon(modifier)
    }
}


@Composable
@Preview
private fun ThunderIconPreview() {
    WeatherTheme {

        val sizeIcon = 64.dp

        Column {
            SunIcon(modifier = Modifier.size(sizeIcon))
            MoonIcon(modifier = Modifier.size(sizeIcon))
            TwoCloudIcon(modifier = Modifier.size(sizeIcon))
            PartlyCloudIcon(modifier = Modifier.size(sizeIcon))
            PartlyCloudIcon(modifier = Modifier.size(sizeIcon), isNight = true)
            RainCloudIcon(modifier = Modifier.size(sizeIcon))
            ThunderCloudIcon(modifier = Modifier.size(sizeIcon))
            SnowCloudIcon(modifier = Modifier.size(sizeIcon))
            FogIcon(modifier = Modifier.size(sizeIcon))
            SleetCloudIcon(modifier = Modifier.size(sizeIcon))
        }
    }
}