package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.WindItem
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

data class WindSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.Wind,
    override val title: Int = R.string.wind_title,
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
        val today = weather.today

        CollapsableContainer(
            headerHeight = headerSectionHeight,
            headerTitle = stringResource(title),
            headerIcon = icon,
            currentBodyHeight = bodyHeight,
            onContentMeasured = measureContainerHeight
        ) {
            WindItem(
                modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
                windSpeed = today.windSpeed,
                gustSpeed = today.gustSpeed,
                degree = today.windDegree,
                direction = today.windDirection,
                windSpeedUnit = appSettings.windSpeedUnit
            )
        }
    }
}