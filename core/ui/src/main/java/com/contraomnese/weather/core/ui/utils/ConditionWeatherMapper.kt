package com.contraomnese.weather.core.ui.utils

import androidx.annotation.StringRes
import com.contraomnese.weather.design.R

private val conditionCodeMap = mapOf(
    1000 to R.string.condition_code_1000,
    1003 to R.string.condition_code_1003,
    1006 to R.string.condition_code_1006,
    1009 to R.string.condition_code_1009,
    1030 to R.string.condition_code_1030,
    1063 to R.string.condition_code_1063,
    1066 to R.string.condition_code_1066,
    1069 to R.string.condition_code_1069,
    1072 to R.string.condition_code_1072,
    1087 to R.string.condition_code_1087,
    1114 to R.string.condition_code_1114,
    1117 to R.string.condition_code_1117,
    1135 to R.string.condition_code_1135,
    1147 to R.string.condition_code_1147,
    1150 to R.string.condition_code_1150,
    1153 to R.string.condition_code_1153,
    1168 to R.string.condition_code_1168,
    1171 to R.string.condition_code_1171,
    1180 to R.string.condition_code_1180,
    1183 to R.string.condition_code_1183,
    1186 to R.string.condition_code_1186,
    1189 to R.string.condition_code_1189,
    1192 to R.string.condition_code_1192,
    1195 to R.string.condition_code_1195,
    1198 to R.string.condition_code_1198,
    1201 to R.string.condition_code_1201,
    1204 to R.string.condition_code_1204,
    1207 to R.string.condition_code_1207,
    1210 to R.string.condition_code_1210,
    1213 to R.string.condition_code_1213,
    1216 to R.string.condition_code_1216,
    1219 to R.string.condition_code_1219,
    1222 to R.string.condition_code_1222,
    1225 to R.string.condition_code_1225,
    1237 to R.string.condition_code_1237,
    1240 to R.string.condition_code_1240,
    1243 to R.string.condition_code_1243,
    1246 to R.string.condition_code_1246,
    1249 to R.string.condition_code_1249,
    1252 to R.string.condition_code_1252,
    1255 to R.string.condition_code_1255,
    1258 to R.string.condition_code_1258,
    1261 to R.string.condition_code_1261,
    1264 to R.string.condition_code_1264,
    1273 to R.string.condition_code_1273,
    1276 to R.string.condition_code_1276,
    1279 to R.string.condition_code_1279,
    1282 to R.string.condition_code_1282
)

@StringRes
fun Int.toConditionWeatherRes(): Int =
    conditionCodeMap[this] ?: R.string.empty