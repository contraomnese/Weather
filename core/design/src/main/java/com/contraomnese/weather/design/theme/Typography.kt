package com.contraomnese.weather.design.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val weatherTypo = Typography(
    displayLarge = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Thin,
        fontSize = 102.sp,
        letterSpacing = (-0.51f).sp
    ),
    displayMedium = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Light,
        fontSize = 53.sp,
        letterSpacing = 5.565f.sp
    ),
    displaySmall = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 37.sp,
        letterSpacing = (-0.185f).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    titleLarge = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        letterSpacing = (-0.0625f).sp
    ),
    titleMedium = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.72f.sp,
        lineHeight = 20.0f.sp
    ),
    bodySmall = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        letterSpacing = 0.51f.sp
    ),
    labelMedium = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp
    ),
    labelSmall = TextStyle(
        fontFamily = sfProDisplayFamily,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.75f.sp,
        lineHeight = 20.0f.sp
    )
)