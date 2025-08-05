package com.contraomnese.weather.presentation.notification

import kotlinx.coroutines.CoroutineScope

typealias NotificationMonitorProvider = @JvmSuppressWildcards (coroutineScope: CoroutineScope) -> NotificationMonitor