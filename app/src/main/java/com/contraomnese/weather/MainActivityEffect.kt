package com.contraomnese.weather

import com.contraomnese.weather.presentation.architecture.MviEffect

internal sealed interface MainActivityEffect : MviEffect {
    data object LoadingFinished : MainActivityEffect
    data class WeatherDestinationIdChanged(val id: Int?) : MainActivityEffect
    data class ForecastAutoSyncChanged(val enabled: Boolean) : MainActivityEffect
}