package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.core.ui.widgets.AirQualityItem
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

data class AqiSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Aqi,
    override val title: Int = R.string.aqi_title,
) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) =
        copy(bodyHeight = newHeight, bodyMaxHeight = bodyMaxHeight ?: newHeight)

    @Composable
    override fun Render(
        headerSectionHeight: Dp,
        weather: Forecast,
        appSettings: AppSettings,
        measureContainerHeight: (Int) -> Unit,
        progress: Float,
    ) {
        CollapsableContainer(
            headerHeight = headerSectionHeight,
            headerTitle = stringResource(title),
            headerIcon = icon,
            currentBodyHeight = bodyHeight,
            onContentMeasured = measureContainerHeight
        ) {
            AirQualityItem(
                modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
                aqiIndex = weather.today.airQuality.aqiIndex,
                coLevel = weather.today.airQuality.coLevel,
                no2Level = weather.today.airQuality.no2Level,
                o3Level = weather.today.airQuality.o3Level,
                so2Level = weather.today.airQuality.so2Level,
                pm25Level = weather.today.airQuality.pm25Level,
                pm10Level = weather.today.airQuality.pm10Level,
            )
        }
    }
}