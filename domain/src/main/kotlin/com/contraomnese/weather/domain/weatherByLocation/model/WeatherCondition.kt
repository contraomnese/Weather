package com.contraomnese.weather.domain.weatherByLocation.model

enum class WeatherCondition {
    CLEAR, PARTLY_CLOUDY, CLOUDY, FOG, RAIN, SNOW, THUNDER, SLEET;

    companion object {
        fun fromConditionCode(code: Int): WeatherCondition {
            return when (code) {
                1000 -> CLEAR
                1003 -> PARTLY_CLOUDY
                1006, 1009 -> CLOUDY
                1030, 1135, 1147 -> FOG
                1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195,
                1240, 1243, 1246, 1273, 1276,
                    -> RAIN

                1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225,
                1255, 1258, 1279, 1282,
                    -> SNOW

                1087 -> THUNDER

                1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207,
                1237, 1249, 1252, 1261, 1264,
                    -> SLEET

                else -> CLEAR

            }
        }
    }
}