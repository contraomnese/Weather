package com.contraomnese.weather.appsettings.presentation

import com.contraomnese.weather.presentation.architecture.MviEvent

internal sealed interface AppSettingsEvent : MviEvent {
    data class HandleError(val cause: Throwable) : AppSettingsEvent
}