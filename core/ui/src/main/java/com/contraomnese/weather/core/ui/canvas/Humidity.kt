package com.contraomnese.weather.core.ui.canvas

import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.contraomnese.weather.design.R
import com.contraomnese.weather.design.theme.WeatherTheme
import kotlin.math.min

@Composable
fun HumidityLevel(
    modifier: Modifier = Modifier,
    value: Int,
) {

    val density = LocalDensity.current
    var width by remember(key1 = value) { mutableIntStateOf(0) }
    var height by remember(key1 = value) { mutableIntStateOf(0) }
    val textMeasurer = rememberTextMeasurer()

    val humidityTextLayout = textMeasurer.measure(
        text = AnnotatedString(stringResource(R.string.humidity_value, value)),
        style = MaterialTheme.typography.headlineMedium.copy(
            color = Color.White,
            fontSize = with(density) { (min(width, height) / 5).toSp() },
            lineBreak = LineBreak.Heading,
            textAlign = TextAlign.Center
        ),
        maxLines = 1,
        constraints = Constraints(maxWidth = width)
    )

    Canvas(modifier = modifier.onSizeChanged {
        width = it.width
        height = it.height
    }) {
        val radius = size.height / 2.5f
        val strokeWidth = radius * 0.04f

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                style = android.graphics.Paint.Style.FILL
                color = android.graphics.Color.argb(150, 100, 150, 255)
            }

            val circlePath = Path().apply {
                addOval(
                    RectF(
                        center.x - radius,
                        center.y - radius,
                        center.x + radius,
                        center.y + radius
                    ),
                    android.graphics.Path.Direction.CW
                )
            }

            val fillHeight = (value / 100f) * radius * 2f
            val levelY = center.y + radius - fillHeight

            val levelPath = Path().apply {
                moveTo(center.x - radius - 40f, center.y + radius + 10f)
                lineTo(center.x - radius - 40f, levelY)
                quadTo(center.x - radius / 2f, levelY - radius * 0.2f, center.x, levelY)
                quadTo(center.x + radius / 2f, levelY + radius * 0.2f, center.x + radius + 40f, levelY)
                lineTo(center.x + radius + 40f, center.y + radius + 10f)
                close()
            }


            canvas.nativeCanvas.save()
            canvas.nativeCanvas.clipPath(circlePath)
            canvas.nativeCanvas.drawPath(levelPath, paint)
            canvas.nativeCanvas.restore()
        }

        drawCircle(
            color = Color.White,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        drawContext.canvas.nativeCanvas.apply {

            drawText(
                humidityTextLayout,
                topLeft = Offset(
                    (size.width - humidityTextLayout.size.width) / 2,
                    (size.height - humidityTextLayout.size.height) / 2
                )
            )
        }
    }
}

@Preview
@Composable
private fun HumidityLevelPreview() {
    WeatherTheme {
        HumidityLevel(
            modifier = Modifier.size(200.dp),
            value = 15
        )
    }
}