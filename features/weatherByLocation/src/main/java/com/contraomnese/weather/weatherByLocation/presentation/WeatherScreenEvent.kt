package com.contraomnese.weather.weatherByLocation.presentation

import com.contraomnese.weather.presentation.architecture.MviEvent


internal sealed interface WeatherScreenEvent : MviEvent {
    data object NavigateToHome : WeatherScreenEvent
    data class HandleError(val cause: Throwable) : WeatherScreenEvent
}