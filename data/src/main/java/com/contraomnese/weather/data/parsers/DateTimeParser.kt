package com.contraomnese.weather.data.parsers

import android.util.Log
import com.contraomnese.weather.domain.weatherByLocation.model.internal.LocationDateTime
import com.contraomnese.weather.domain.weatherByLocation.model.internal.LocationTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

object DateTimeParser {

    private val isoFormatter = LocalDateTime.Format {
        date(LocalDate.Formats.ISO)
        char(' ')
        time(LocalTime.Formats.ISO)
    }

    private val amPmFormatter = LocalTime.Format {
        amPmHour(); char(':'); minute(); char(' '); amPmMarker("AM", "PM")
    }

    fun parseIso(input: String): LocationDateTime? {
        return try {
            val dateTime = LocalDateTime.parse(input, isoFormatter)
            LocationDateTime(dateTime)
        } catch (e: Exception) {
            Log.e("DateTimeParser", "Parse error for input=$input", e)
            null
        }
    }

    fun parseAmPmTime(input: String): LocationTime? {
        return try {
            val time = LocalTime.parse(input, amPmFormatter)
            LocationTime(time)
        } catch (e: Exception) {
            Log.e("DateTimeParser", "Parse error for input=$input", e)
            null
        }
    }
}