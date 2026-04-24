package com.contraomnese.weather.domain.weatherByLocation.model

enum class WeatherCondition {
    CLEAR,
    PARTLY_CLOUDY,
    CLOUDY,
    OVERCAST,
    FOG,
    FREEZING_FOG,
    RAIN_SLIGHT,
    DRIZZLE_LIGHT,
    DRIZZLE_MODERATE,
    DRIZZLE_HEAVY,
    FREEZING_DRIZZLE_LIGHT,
    FREEZING_DRIZZLE_HEAVY,
    FREEZING_RAIN_LIGHT,
    FREEZING_RAIN_HEAVY,
    SNOW_FALL_SLIGHT,
    SNOW_FALL_MODERATE,
    SNOW_FALL_HEAVY,
    RAIN_MODERATE,
    RAIN_HEAVY,
    RAIN_SHOWERS_SLIGHT,
    RAIN_SHOWERS_MODERATE,
    RAIN_SHOWERS_HEAVY,
    SNOW_SHOWERS_LIGHT,
    SNOW_SHOWERS_HEAVY,
    THUNDERSTORM,
    THUNDERSTORM_WITH_RAIN_LIGHT,
    THUNDERSTORM_WITH_RAIN_HEAVY,
    UNKNOWN;

    companion object {
        fun fromWeatherApi(code: Int): WeatherCondition {
            return when (code) {
                1000, 0 -> CLEAR
                1003, 1 -> PARTLY_CLOUDY
                1006, 2 -> CLOUDY
                1009, 3 -> OVERCAST

                1030, 1135, 45 -> FOG
                1147, 48 -> FREEZING_FOG

                1150, 1153, 51 -> DRIZZLE_LIGHT
                53 -> DRIZZLE_MODERATE
                55 -> DRIZZLE_HEAVY

                1204, 1072, 1168, 56 -> FREEZING_DRIZZLE_LIGHT
                1207, 1171, 57 -> FREEZING_DRIZZLE_HEAVY

                1063, 1180, 1183, 61 -> RAIN_SLIGHT
                1186, 1189, 63 -> RAIN_MODERATE
                1192, 1195, 65 -> RAIN_HEAVY

                1069, 1198, 1249, 1261, 66 -> FREEZING_RAIN_LIGHT
                1201, 1237, 1252, 1264, 67 -> FREEZING_RAIN_HEAVY

                1066, 1210, 1213, 71 -> SNOW_FALL_SLIGHT
                1114, 1216, 1219, 73 -> SNOW_FALL_MODERATE
                1222, 1225, 1117, 75 -> SNOW_FALL_HEAVY

                1240, 80 -> RAIN_SHOWERS_SLIGHT
                1243, 81 -> RAIN_SHOWERS_MODERATE
                1246, 82 -> RAIN_SHOWERS_HEAVY

                1255, 77, 85 -> SNOW_SHOWERS_LIGHT
                1258, 86 -> SNOW_SHOWERS_HEAVY

                1087, 95 -> THUNDERSTORM

                1273, 1279, 96 -> THUNDERSTORM_WITH_RAIN_LIGHT
                1276, 1282, 99 -> THUNDERSTORM_WITH_RAIN_HEAVY

                else -> UNKNOWN

            }
        }
    }
}