package com.contraomnese.weather.data.mappers

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun getDayOfWeek(
    epochSeconds: Long,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    locale: Locale = Locale.getDefault(),
): String {
    val instant = Instant.fromEpochSeconds(epochSeconds)
    val millis = instant.toEpochMilliseconds()

    val sdf = SimpleDateFormat("EEE", locale)
    sdf.timeZone = java.util.TimeZone.getTimeZone(timeZone.id)

    return sdf.format(Date(millis)).uppercase()
}