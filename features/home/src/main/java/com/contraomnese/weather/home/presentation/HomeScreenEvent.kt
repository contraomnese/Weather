package com.contraomnese.weather.home.presentation

import com.contraomnese.weather.presentation.architecture.MviEvent

internal sealed interface HomeScreenEvent : MviEvent {
    data object GetGpsLocation : HomeScreenEvent
    data class HandleError(val cause: Throwable) : HomeScreenEvent
    data class NavigateToGpsLocation(val id: Int) : HomeScreenEvent
}