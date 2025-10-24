package com.contraomnese.weather.weatherByLocation.presentation.sections

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.weatherByLocation.model.Forecast

internal interface WeatherSection {

    val bodyHeight: Float?
    val bodyMaxHeight: Float?
    val icon: ImageVector

    @get:StringRes
    val title: Int

    fun copyWithBodyHeight(newHeight: Float): WeatherSection

    operator fun component1(): Float? = bodyHeight
    operator fun component2(): Float? = bodyMaxHeight

    @Composable
    fun Render(
        headerSectionHeight: Dp,
        weather: Forecast,
        appSettings: AppSettings,
        measureContainerHeight: (Int) -> Unit,
        progress: Float = 0f,
    )
}