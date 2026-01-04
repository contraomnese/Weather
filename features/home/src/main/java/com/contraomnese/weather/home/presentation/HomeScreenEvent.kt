package com.contraomnese.weather.home.presentation

import com.contraomnese.weather.presentation.architecture.MviEvent

internal sealed interface HomeScreenEvent : MviEvent {
    data class SwitchGpsMode(val enabled: Boolean) : HomeScreenEvent
    data object GetGpsLocation : HomeScreenEvent
    data class OnSearchBarTop(val onTop: Boolean) : HomeScreenEvent
    data class HandleError(val cause: Throwable) : HomeScreenEvent
    data class NavigateToGpsLocation(val id: Int) : HomeScreenEvent
}