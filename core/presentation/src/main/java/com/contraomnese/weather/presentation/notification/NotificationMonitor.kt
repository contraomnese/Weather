package com.contraomnese.weather.presentation.notification

import androidx.annotation.StringRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class NotificationMonitor {

    private val _notificationChannel = Channel<Int>(Channel.BUFFERED)
    val notifications = _notificationChannel.receiveAsFlow()

    suspend fun emit(@StringRes messageResId: Int) {
        _notificationChannel.send(messageResId)
    }

    fun tryEmit(@StringRes messageResId: Int) {
        _notificationChannel.trySend(messageResId)
    }
}