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
        offset > screenWidth * 0.25f && currentFavoriteIndex > 0 -> {
            onDragPrev()
            animateDrag(coroutineScope, scaleAnimated, alphaAnimated)
        }

        offset < -screenWidth * 0.25f && currentFavoriteIndex < lastFavoriteIndex -> {
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