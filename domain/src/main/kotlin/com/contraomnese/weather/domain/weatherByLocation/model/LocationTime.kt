package com.contraomnese.weather.domain.weatherByLocation.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

private val astroFormatter = LocalTime.Format {
    amPmHour(padding = Padding.NONE); char(':'); minute(); char(' '); amPmMarker("AM", "PM")
}

private val localTimeFormatter = LocalTime.Format {
    hour()
    char(':')
    minute()
}

data class LocationTime(
    val time: LocalTime,
) {

    companion object {
        fun fromEpochSeconds(epochSeconds: Long, timeZone: TimeZone): LocationTime {
            return LocationTime(Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(timeZone).time)
        }

        fun fromInstant(instant: Instant, timeZone: TimeZone): LocationTime {
            return LocationTime(instant.toLocalDateTime(timeZone).time)
        }

        fun parseAmPmTime(input: String): LocationTime {
            val time = LocalTime.parse(input, astroFormatter)
            return LocationTime(time)
        }
    }

    fun toLocalTime(): String {
        return time.format(localTimeFormatter)
    }

    fun toMinutes(): Int {
        return time.hour * 60 + time.minute
    }
}