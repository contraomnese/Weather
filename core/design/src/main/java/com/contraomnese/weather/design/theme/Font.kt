package com.contraomnese.weather.design.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.contraomnese.weather.design.R

private val font = R.font.inter_variable

val sfProDisplayFamily = FontFamily(
    Font(resId = font, style = FontStyle.Normal, weight = FontWeight.Thin),
    Font(resId = font, style = FontStyle.Normal, weight = FontWeight.Light),
    Font(resId = font, style = FontStyle.Normal, weight = FontWeight.Normal),
    Font(resId = font, style = FontStyle.Normal, weight = FontWeight.Medium),
    Font(resId = font, style = FontStyle.Normal, weight = FontWeight.Bold),
)