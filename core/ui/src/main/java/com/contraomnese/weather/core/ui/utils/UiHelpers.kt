package com.contraomnese.weather.core.ui.utils

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.contraomnese.weather.core.ui.composition.WeatherBackground
import com.contraomnese.weather.design.R
import com.contraomnese.weather.domain.weatherByLocation.model.WeatherCondition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

fun extractBottomColor(bitmap: Bitmap): Color {
    val y = bitmap.height - 1
    val pixels = IntArray(bitmap.width)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, y, bitmap.width, 1)

    val avgRed = pixels.map { (it shr 16) and 0xFF }.average().toInt()
    val avgGreen = pixels.map { (it shr 8) and 0xFF }.average().toInt()
    val avgBlue = pixels.map { it and 0xFF }.average().toInt()

    return Color(avgRed, avgGreen, avgBlue)
}

fun animateDrag(
    coroutineScope: CoroutineScope,
    scaleAnimatable: Animatable<Float, AnimationVector1D>,
    alphaAnimatable: Animatable<Float, AnimationVector1D>,
) {
    coroutineScope.launch {
        launch {
            scaleAnimatable.animateTo(0.8f, tween(300))
        }
        launch {
            alphaAnimatable.snapTo(0f)
        }
        launch {
            scaleAnimatable.animateTo(1f, tween(300))
            alphaAnimatable.animateTo(1f, tween(600))
        }
    }
}

fun handleHorizontalDragEnd(
    offset: Float,
    onOffsetChange: (Float) -> Unit,
    currentFavoriteIndex: Int,
    lastFavoriteIndex: Int,
    screenWidth: Float,
    onDragNext: () -> Unit,
    onDragPrev: () -> Unit,
    coroutineScope: CoroutineScope,
    scaleAnimated: Animatable<Float, AnimationVector1D>,
    alphaAnimated: Animatable<Float, AnimationVector1D>,
) {
    when {
        offset > screenWidth * 0.15f && currentFavoriteIndex > 0 -> {
            onDragPrev()
            animateDrag(coroutineScope, scaleAnimated, alphaAnimated)
        }

        offset < -screenWidth * 0.15f && currentFavoriteIndex < lastFavoriteIndex -> {
            onDragNext()
            animateDrag(coroutineScope, scaleAnimated, alphaAnimated)
        }
    }
    coroutineScope.launch {
        animate(
            initialValue = offset,
            targetValue = 0f,
            animationSpec = tween(600)
        ) { value, _ -> onOffsetChange(value) }
    }
}

@Composable
fun WeatherCondition.getBackground(): WeatherBackground = when (this) {
    WeatherCondition.CLEAR -> WeatherBackground(R.drawable.clear, Color(0xFF302320))
    WeatherCondition.PARTLY_CLOUDY -> WeatherBackground(R.drawable.partly_cloud, Color(0xFF788088))
    WeatherCondition.CLOUDY, WeatherCondition.OVERCAST -> WeatherBackground(R.drawable.overcast, Color(0xFF999B9D))
    WeatherCondition.FOG -> WeatherBackground(R.drawable.fog, Color(0xFF676767))
    WeatherCondition.FREEZING_FOG -> WeatherBackground(R.drawable.snow, Color(0xFFE9F0F6))
    WeatherCondition.DRIZZLE_LIGHT, WeatherCondition.DRIZZLE_MODERATE, WeatherCondition.DRIZZLE_HEAVY -> WeatherBackground(
        R.drawable.rain,
        Color(0xFF485043)
    )

    WeatherCondition.FREEZING_DRIZZLE_LIGHT, WeatherCondition.FREEZING_RAIN_LIGHT, WeatherCondition.FREEZING_DRIZZLE_HEAVY, WeatherCondition.FREEZING_RAIN_HEAVY -> WeatherBackground(
        R.drawable.sleet,
        Color(0xFF0F1D24)
    )

    WeatherCondition.SNOW_FALL_SLIGHT, WeatherCondition.SNOW_FALL_MODERATE, WeatherCondition.SNOW_FALL_HEAVY -> WeatherBackground(
        R.drawable.snow,
        Color(0xFFE9F0F6)
    )

    WeatherCondition.RAIN_SLIGHT, WeatherCondition.RAIN_MODERATE, WeatherCondition.RAIN_HEAVY, WeatherCondition.SNOW_SHOWERS_LIGHT, WeatherCondition.SNOW_SHOWERS_HEAVY -> WeatherBackground(
        R.drawable.rain,
        Color(0xFF485043)
    )

    WeatherCondition.RAIN_SHOWERS_SLIGHT, WeatherCondition.RAIN_SHOWERS_MODERATE, WeatherCondition.RAIN_SHOWERS_HEAVY -> WeatherBackground(
        R.drawable.rain,
        Color(0xFF485043)
    )

    WeatherCondition.THUNDERSTORM, WeatherCondition.THUNDERSTORM_WITH_RAIN_LIGHT, WeatherCondition.THUNDERSTORM_WITH_RAIN_HEAVY -> WeatherBackground(
        R.drawable.thunder,
        Color(0xFF6276A0)
    )

    WeatherCondition.UNKNOWN -> WeatherBackground(R.drawable.thunder, Color(0xFF6276A0))
}

fun WeatherCondition.getConditionText(): Int = when (this) {
    WeatherCondition.CLEAR -> R.string.wmo_clear_sky
    WeatherCondition.PARTLY_CLOUDY -> R.string.wmo_partly_cloudy
    WeatherCondition.CLOUDY -> R.string.wmo_partly_cloudy
    WeatherCondition.OVERCAST -> R.string.wmo_overcast
    WeatherCondition.FOG -> R.string.wmo_fog
    WeatherCondition.FREEZING_FOG -> R.string.wmo_freezing_fog
    WeatherCondition.RAIN_SLIGHT -> R.string.wmo_rain_slight
    WeatherCondition.DRIZZLE_LIGHT -> R.string.wmo_drizzle_light
    WeatherCondition.DRIZZLE_MODERATE -> R.string.wmo_drizzle_moderate
    WeatherCondition.DRIZZLE_HEAVY -> R.string.wmo_drizzle_dense
    WeatherCondition.FREEZING_DRIZZLE_LIGHT -> R.string.wmo_freezing_drizzle_light
    WeatherCondition.FREEZING_DRIZZLE_HEAVY -> R.string.wmo_freezing_drizzle_dense
    WeatherCondition.FREEZING_RAIN_LIGHT -> R.string.wmo_freezing_rain_light
    WeatherCondition.FREEZING_RAIN_HEAVY -> R.string.wmo_freezing_rain_heavy
    WeatherCondition.SNOW_FALL_SLIGHT -> R.string.wmo_snow_fall_slight
    WeatherCondition.SNOW_FALL_MODERATE -> R.string.wmo_snow_fall_moderate
    WeatherCondition.SNOW_FALL_HEAVY -> R.string.wmo_snow_fall_heavy
    WeatherCondition.RAIN_MODERATE -> R.string.wmo_rain_moderate
    WeatherCondition.RAIN_HEAVY -> R.string.wmo_rain_heavy
    WeatherCondition.RAIN_SHOWERS_SLIGHT -> R.string.wmo_rain_showers_slight
    WeatherCondition.RAIN_SHOWERS_MODERATE -> R.string.wmo_rain_showers_moderate
    WeatherCondition.RAIN_SHOWERS_HEAVY -> R.string.wmo_rain_showers_violent
    WeatherCondition.SNOW_SHOWERS_LIGHT -> R.string.wmo_snow_showers_slight
    WeatherCondition.SNOW_SHOWERS_HEAVY -> R.string.wmo_snow_showers_heavy
    WeatherCondition.THUNDERSTORM -> R.string.wmo_thunderstorm_slight
    WeatherCondition.THUNDERSTORM_WITH_RAIN_LIGHT -> R.string.wmo_thunderstorm_hail_slight
    WeatherCondition.THUNDERSTORM_WITH_RAIN_HEAVY -> R.string.wmo_thunderstorm_hail_heavy
    WeatherCondition.UNKNOWN -> TODO()
}