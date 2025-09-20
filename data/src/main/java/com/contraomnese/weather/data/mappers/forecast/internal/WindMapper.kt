package com.contraomnese.weather.data.mappers.forecast.internal

private val directions = mapOf(
    "N" to "С",
    "S" to "Ю",
    "E" to "В",
    "W" to "З"
)

internal fun String.translateDirection(): String =
    this.map { directions[it.toString()] ?: it.toString() }
        .joinToString("")