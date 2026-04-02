package com.contraomnese.weather.data.mappers.utils

private const val MM_IN_INCH = 25.4
private const val KPH_TO_MPH = 1.6093
private const val KPH_TO_MS = 0.277778
private const val HPA_TO_IN_HG = 0.0295300586467
private const val HPA_TO_MM_HG = 0.75
private const val KM_TO_MILES = 0.621371

internal fun celsiusToFahrenheit(value: Double): Double {
    return (value * 9 / 5) + 32
}

internal fun kphToMph(value: Double): Double {
    return value * KPH_TO_MPH
}

internal fun kphToMs(value: Double): Double {
    return value * KPH_TO_MS
}

internal fun mphToKph(value: Double): Double {
    return value * KPH_TO_MPH
}

internal fun mmToInch(value: Double): Double {
    return value / MM_IN_INCH
}

internal fun inchToMm(value: Double): Double {
    return value * MM_IN_INCH
}

internal fun hPaToInchesHg(value: Double): Double {
    return value * HPA_TO_IN_HG
}

internal fun hPaToMmHg(value: Double): Double {
    return value * HPA_TO_MM_HG
}

internal fun kmToMiles(value: Double): Double {
    return value * KM_TO_MILES
}