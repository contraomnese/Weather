package com.contraomnese.weather

import com.contraomnese.weather.presentation.architecture.MviEvent

internal sealed interface MainActivityEvent : MviEvent {
    data class HandleError(val cause: Throwable) : MainActivityEvent
}