package com.contraomnese.weather.core.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.theme.WeatherTheme

@Composable
fun MoonIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFA8A439),
    starColor: Color = Color(0xFFDEDC99),
) {
    Canvas(modifier) {
        val radius = size.minDimension / 2.5f
        val earthShadowCenter = Offset(center.x * 1.5f, center.y * 0.7f)

        val drawMoon: DrawScope.() -> Unit = {
            drawCircle(
                color = color,
                radius = radius,
                center = center
            )
        }
        val earthShadow = Path().apply {
            addOval(
                Rect(
                    center = earthShadowCenter,
                    radius = radius
                )
            )
        }
        clipPath(earthShadow, clipOp = ClipOp.Difference) {
            drawMoon()
        }

        val stars = listOf(
            Pair(1.5f to 1f, 1f / 4f),
            Pair(1.35f to 0.4f, 1f / 5f),
            Pair(1f to 0.8f, 1f / 6f)
        )

        stars.forEach { (posRatio, sizeRatio) ->
            val (rx, ry) = posRatio
            val centerOffset = Offset(center.x * rx, center.y * ry)
            val starSize = radius * sizeRatio
            val starPath = star(centerOffset, starSize)
            drawPath(path = starPath, color = starColor, style = Fill)
        }
    }
}

private fun star(starCenter: Offset, baseR: Float): Path {

    val verticalStretch = 1.2f

    val star = Path().apply {
        val topLeft = Rect(
            left = starCenter.x - baseR * 2,
            top = starCenter.y - baseR * 2 * verticalStretch,
            right = starCenter.x,
            bottom = starCenter.y
        )
        moveTo(starCenter.x, starCenter.y)
        arcTo(topLeft, startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false)
        lineTo(starCenter.x, starCenter.y)
        close()

        val bottomLeft = Rect(
            left = starCenter.x - baseR * 2,
            top = starCenter.y,
            right = starCenter.x,
            bottom = starCenter.y + baseR * 2 * verticalStretch
        )
        moveTo(starCenter.x, starCenter.y)
        arcTo(bottomLeft, startAngleDegrees = -90f, sweepAngleDegrees = 90f, forceMoveTo = false)
        lineTo(starCenter.x, starCenter.y)
        close()

        val bottomRight = Rect(
            left = starCenter.x,
            top = starCenter.y,
            right = starCenter.x + baseR * 2,
            bottom = starCenter.y + baseR * 2 * verticalStretch
        )
        moveTo(starCenter.x, starCenter.y)
        arcTo(bottomRight, startAngleDegrees = -180f, sweepAngleDegrees = 90f, forceMoveTo = false)
        lineTo(starCenter.x, starCenter.y)
        close()

        val topRight = Rect(
            left = starCenter.x,
            top = starCenter.y - baseR * 2 * verticalStretch,
            right = starCenter.x + baseR * 2,
            bottom = starCenter.y
        )
        moveTo(starCenter.x, starCenter.y)
        arcTo(topRight, startAngleDegrees = -270f, sweepAngleDegrees = 90f, forceMoveTo = false)
        lineTo(starCenter.x, starCenter.y)
        close()
    }
    return star
}

@Preview
@Composable
private fun MoonIconPreview() {
    WeatherTheme {
        MoonIcon(Modifier.size(200.dp))
    }
}