package com.contraomnese.weather.domain.weatherByLocation.model.internal

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

data class LocationTime(
    val time: LocalTime,
) {
    private val localTimeFormatter = LocalTime.Format {
        hour()
        char(':')
        minute()
    }

    fun toLocalTime(): String {
        return time.format(localTimeFormatter)
    }

    fun toMinutes(): Int {
        return time.hour * 60 + time.minute
    }
}