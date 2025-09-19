package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme
import androidx.compose.ui.graphics.Color as ComposeColor

enum class DewPointState {
    Happy, Neutral, Sad;

    companion object {
        fun fromDewPointC(value: Int): DewPointState {
            return when (value) {
                in 0..10 -> Happy
                in 11..18 -> Neutral
                else -> Sad
            }
        }

        fun fromDewPointF(value: Int): DewPointState {
            return when (value) {
                in 0..10 -> Happy
                in 11..18 -> Neutral
                else -> Sad
            }
        }
    }
}

@Composable
fun DewPointEmoji(
    modifier: Modifier = Modifier,
    state: DewPointState,
) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(ComposeColor(0xFFFFD93B), ComposeColor(0xFFEFAC40), ComposeColor(0xFFDEB435)),
                center = center,
                radius = radius * 2
            ),
            radius = radius,
            center = center
        )

        val eyeOffsetX = radius * 0.35f
        val eyeOffsetY = radius * -0.2f
        val eyeRadius = radius * 0.25f

        val leftEyeCenter = Offset(center.x - eyeOffsetX, center.y + eyeOffsetY)
        val rightEyeCenter = Offset(center.x + eyeOffsetX, center.y + eyeOffsetY)

        drawCircle(ComposeColor.White, radius = eyeRadius, center = leftEyeCenter)
        drawCircle(ComposeColor.White, radius = eyeRadius, center = rightEyeCenter)

        val pupilRadius = eyeRadius * 0.4f
        val pupilLeftOffset = Offset(leftEyeCenter.x - eyeOffsetX * 0.1f, leftEyeCenter.y - eyeOffsetY * 0.1f)
        val pupilRightOffset = Offset(rightEyeCenter.x - eyeOffsetX * 0.1f, rightEyeCenter.y - eyeOffsetY * 0.1f)

        drawCircle(ComposeColor.Black, radius = pupilRadius, center = pupilLeftOffset)
        drawCircle(ComposeColor.Black, radius = pupilRadius, center = pupilRightOffset)

        val mouthWidth = radius * 1.2f
        val mouthHeight = radius * 0.4f
        val mouthTop = center.y + radius * 0.4f

        val mouthPath = Path().apply {
            when (state) {
                DewPointState.Happy -> {
                    moveTo(center.x - mouthWidth / 2, mouthTop)
                    quadraticTo(
                        center.x, mouthTop + mouthHeight,
                        center.x + mouthWidth / 2, mouthTop
                    )
                }

                DewPointState.Neutral -> {
                    moveTo(center.x - mouthWidth / 2, mouthTop)
                    lineTo(center.x + mouthWidth / 2, mouthTop)
                }

                DewPointState.Sad -> {
                    moveTo(center.x - mouthWidth / 2, mouthTop)
                    quadraticTo(
                        center.x, mouthTop - mouthHeight,
                        center.x + mouthWidth / 2, mouthTop
                    )
                }
            }
        }

        drawPath(
            path = mouthPath,
            color = ComposeColor.Black,
            style = Stroke(width = radius * 0.1f, cap = StrokeCap.Round)
        )
    }
}

@Preview
@Composable
private fun DewPointEmojiPreview() {
    WeatherTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            DewPointEmoji(
                modifier = Modifier.size(120.dp),
                state = DewPointState.Happy
            )
            DewPointEmoji(
                modifier = Modifier.size(120.dp),
                state = DewPointState.Neutral
            )
            DewPointEmoji(
                modifier = Modifier.size(120.dp),
                state = DewPointState.Sad
            )
        }
    }
}
