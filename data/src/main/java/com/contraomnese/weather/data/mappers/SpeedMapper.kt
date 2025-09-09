package com.contraomnese.weather.data.mappers

internal fun Double.toMs(): Double {
    return this * 1000.0 / 3600.0
}