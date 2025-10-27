package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.core.ui.widgets.CollapsableContainer
import com.contraomnese.weather.core.ui.widgets.ForecastDailyColumn
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.icons.WeatherIcons
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

data class DailyForecastSection(
    override val bodyHeight: MutableState<Float>? = null,
    override val bodyMaxHeight: Float? = bodyHeight?.value,
    override val icon: ImageVector = WeatherIcons.Daily,
    override val title: Int = R.string.daily_forecast_title,
) :
    WeatherSection {
    override fun initBodyHeight(newHeight: Float) =
        copy(
            bodyHeight = mutableFloatStateOf(newHeight),
            bodyMaxHeight = bodyMaxHeight ?: newHeight
        )

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
            currentBodyHeight = bodyHeight?.value,
            onContentMeasured = measureContainerHeight
        ) {
            ForecastDailyColumn(
                modifier = Modifier.padding(horizontal = padding16),
                items = weather.forecast.days,
                currentTemperature = weather.today.temperature.toInt(),
                temperatureUnit = appSettings.temperatureUnit
            )
        }
    }
}