package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

data class LocationDateTime(
    val dateTime: LocalDateTime,
) {

    companion object {
        fun toLocalTimeFromEpochSeconds(epochSeconds: Long, timeZone: TimeZone): String {
            return LocationDateTime(Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(timeZone)).toLocalTime()
        }
    }

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

