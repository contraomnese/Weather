package com.contraomnese.weather.domain.weatherByLocation.model.internal

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

data class LocationDateTime(
    val dateTime: LocalDateTime,
) {
    private val localTimeFormatter = LocalDateTime.Format {
        hour()
        char(':')
        minute()
    }

    fun toLocalTime(): String {
        return dateTime.format(localTimeFormatter)
    }

    fun toMinutes(): Int {
        return dateTime.hour * 60 + dateTime.minute
    }
}