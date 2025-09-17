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

@Composable
fun ConditionsIcon(code: Int, modifier: Modifier = Modifier, isNight: Boolean = false) {
    when (code) {
        1000 -> if (isNight) MoonIcon(modifier) else SunIcon(modifier)

        1003 -> PartlyCloudIcon(modifier)

        1006, 1009 -> TwoCloudIcon(modifier)

        1030, 1135, 1147 -> FogIcon(modifier)

        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195,
        1240, 1243, 1246, 1273, 1276,
            -> RainCloudIcon(modifier)

        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225,
        1255, 1258, 1279, 1282,
            -> SnowCloudIcon(modifier)

        1087 -> ThunderCloudIcon(modifier)

        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207,
        1237, 1249, 1252, 1261, 1264,
            -> SleetCloudIcon(modifier)

        else -> TwoCloudIcon(modifier)
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