package com.contraomnese.weather.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import com.contraomnese.weather.design.theme.WeatherTheme
import com.contraomnese.weather.design.theme.padding16
import com.contraomnese.weather.design.theme.padding80

@Composable
fun BoxScope.AnimatedAutoSizeTitleText(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    visible: Boolean = true,
    enter: EnterTransition,
    exit: ExitTransition,
    title: String,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = 1,
    minFontSize: Int = 24,
) {
    val measurer = rememberTextMeasurer()
    val fontStyle = MaterialTheme.typography.headlineMedium.copy(
        lineBreak = LineBreak.Paragraph
    )
    var fontSize by remember { mutableStateOf(40.sp) }

    AnimatedVisibility(
        modifier = Modifier.align(alignment),
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding16)
        ) {
            val maxWidthPx = constraints.maxWidth.toFloat()

            LaunchedEffect(title, maxWidthPx) {
                var currentSize = fontSize
                while (currentSize.value > minFontSize) {
                    val result = measurer.measure(
                        text = AnnotatedString(title),
                        style = fontStyle.copy(fontSize = currentSize),
                        maxLines = maxLines,
                        constraints = Constraints(
                            maxWidth = maxWidthPx.toInt()
                        )
                    )
                    val fitsWidth = !result.didOverflowWidth
                    val fitsHeight = !result.didOverflowHeight
                    val fitsLines = result.lineCount <= maxLines

                    if (fitsWidth && fitsHeight && fitsLines) break
                    currentSize *= 0.9f
                }
                fontSize = currentSize
            }

            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = title,
                textAlign = textAlign,
                style = fontStyle.copy(fontSize = fontSize),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                maxLines = maxLines,
            )
        }
    }
}


@Preview(showSystemUi = true, device = "id:pixel_5", showBackground = true, backgroundColor = 0xFF17579E)
@Composable
private fun AutoSizeTitleTextPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedAutoSizeTitleText(
                modifier = modifier.padding(top = padding80),
                visible = true,
                enter = expandHorizontally(animationSpec = tween(durationMillis = 500)),
                exit = shrinkHorizontally(animationSpec = tween(durationMillis = 500)),
                title = "Воспользуйтесь поиском",
                maxLines = 2,
                minFontSize = 24
            )
        }
    }
}