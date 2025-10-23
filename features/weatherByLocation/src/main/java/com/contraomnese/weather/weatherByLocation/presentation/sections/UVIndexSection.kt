package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.UvIndexItem
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding8
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

data class UVIndexSection(
    override val bodyHeight: Float? = null,
    override val bodyMaxHeight: Float? = bodyHeight,
    override val icon: ImageVector = WeatherIcons.UvIndex,
    override val title: Int = R.string.uv_title,
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
            UvIndexItem(
                modifier = Modifier.padding(horizontal = padding16, vertical = padding8),
                index = today.uvIndex.value
            )
        }
    }
}